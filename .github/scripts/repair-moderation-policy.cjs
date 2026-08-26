const { StreamClient } = require('@stream-io/node-sdk');

const templateNames = [
  'moderation_template_activity',
  'moderation_template_reaction',
];
const blockListName = 'stream_java_moderation_tests';
const triggerWord = 'pissoar';
const rule = { name: blockListName, action: 'remove' };
const unavailableBlockListNames = new Set(['profanity_en']);
const propagationRetryDelaysMs = [1000, 2000, 4000, 8000];

function sleep(delayMs) {
  return new Promise((resolve) => setTimeout(resolve, delayMs));
}

async function retryAfterBlockListPropagation(operation) {
  for (const delayMs of propagationRetryDelaysMs) {
    try {
      return await operation();
    } catch (error) {
      const message = error instanceof Error ? error.message : String(error);
      if (!message.includes(`Blocklist not found: ${blockListName}`)) {
        throw error;
      }

      console.log(`Waiting ${delayMs}ms for ${blockListName} to propagate`);
      await sleep(delayMs);
    }
  }

  return operation();
}

async function ensureTestBlockList(client) {
  const response = await client.listBlockLists();
  const blockList = response.blocklists.find(
    (candidate) => candidate.name === blockListName,
  );

  if (!blockList) {
    await client.createBlockList({
      name: blockListName,
      type: 'word',
      words: [triggerWord],
    });
    console.log(`Created test blocklist ${blockListName}`);
    return;
  }

  if (!blockList.words.includes(triggerWord)) {
    await client.updateBlockList({
      name: blockListName,
      words: [...blockList.words, triggerWord],
    });
    console.log(`Updated test blocklist ${blockListName}`);
    return;
  }

  console.log(`Test blocklist ${blockListName} already exists`);
}

function withRequiredRule(blockListConfig = {}) {
  const rules = (blockListConfig.rules || []).filter(
    (candidate) => !unavailableBlockListNames.has(candidate.name),
  );
  const index = rules.findIndex((candidate) => candidate.name === rule.name);

  if (index === -1) {
    rules.push(rule);
  } else {
    rules[index] = { ...rules[index], action: rule.action };
  }

  return { ...blockListConfig, rules };
}

function hasRequiredRule(blockListConfig) {
  return blockListConfig?.rules?.some(
    (candidate) =>
      candidate.name === rule.name && candidate.action === rule.action,
  );
}

async function repairPolicy(client, key) {
  const response = await client.moderation.getConfig({ key });
  const config = response.config;
  if (!config) {
    throw new Error(`Moderation policy ${key} was not found`);
  }
  if (hasRequiredRule(config.block_list_config)) {
    console.log(`Moderation policy ${key} already contains the required rule`);
    return;
  }

  const writableFields = [
    'async',
    'team',
    'ai_audio_config',
    'ai_image_config',
    'ai_text_config',
    'ai_video_config',
    'automod_platform_circumvention_config',
    'automod_toxicity_config',
    'aws_rekognition_config',
    'block_list_config',
    'bodyguard_config',
    'flood_config',
    'google_vision_config',
    'llm_config',
    'rule_builder_config',
    'velocity_filter_config',
    'video_call_rule_config',
  ];
  const payload = { key };
  for (const field of writableFields) {
    if (config[field] !== undefined) {
      payload[field] = config[field];
    }
  }
  payload.block_list_config = withRequiredRule(config.block_list_config);

  await retryAfterBlockListPropagation(() =>
    client.moderation.upsertConfig(payload),
  );
  console.log(`Repaired moderation policy ${key}`);
}

async function main() {
  if (!process.env.STREAM_KEY || !process.env.STREAM_SECRET) {
    throw new Error('STREAM_KEY and STREAM_SECRET are required');
  }

  const client = new StreamClient(
    process.env.STREAM_KEY,
    process.env.STREAM_SECRET,
  );
  await ensureTestBlockList(client);
  const response = await client.moderation.v2QueryTemplates();
  const templates = new Map(
    response.templates.map((template) => [template.name, template]),
  );
  const repairedPolicies = new Set();

  for (const name of templateNames) {
    const template = templates.get(name);
    if (!template?.config) {
      throw new Error(`Moderation template ${name} was not found`);
    }

    if (template.config.config_key) {
      if (!repairedPolicies.has(template.config.config_key)) {
        await repairPolicy(client, template.config.config_key);
        repairedPolicies.add(template.config.config_key);
      }
      continue;
    }

    if (hasRequiredRule(template.config.block_list_config)) {
      console.log(`Moderation template ${name} already contains the required rule`);
      continue;
    }

    await retryAfterBlockListPropagation(() =>
      client.moderation.v2UpsertTemplate({
        name,
        config: {
          ...template.config,
          block_list_config: withRequiredRule(template.config.block_list_config),
        },
      }),
    );
    console.log(`Repaired moderation template ${name}`);
  }
}

main().catch((error) => {
  console.error(error instanceof Error ? error.message : error);
  process.exitCode = 1;
});

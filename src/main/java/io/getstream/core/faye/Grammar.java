package io.getstream.core.faye;

import java.util.regex.Pattern;

public class Grammar {
  static final Pattern CHANNEL_NAME =
      Pattern.compile(
          "^\\/(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)))+(\\/(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)))+)*$",
          Pattern.MULTILINE);
  static final Pattern CHANNEL_PATTERN =
      Pattern.compile(
          "^(\\/(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)))+)*\\/\\*{1,2}$");
  static final Pattern ERROR =
      Pattern.compile(
          "^([0-9][0-9][0-9]:(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)| |\\/|\\*|\\.))*(,(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)| |\\/|\\*|\\.))*)*:(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)| |\\/|\\*|\\.))*|[0-9][0-9][0-9]::(((([a-z]|[A-Z])|[0-9])|(\\-|\\_|\\!|\\~|\\(|\\)|\\$|\\@)| |\\/|\\*|\\.))*)$");
  static final Pattern VERSION =
      Pattern.compile("^([0-9])+(\\.(([a-z]|[A-Z])|[0-9])(((([a-z]|[A-Z])|[0-9])|\\-|\\_))*)*$");
}

package io.getstream.core.utils;

import com.google.common.collect.ImmutableList;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.java.lang.DoubleGenerator;
import com.pholser.junit.quickcheck.generator.java.lang.StringGenerator;
import com.pholser.junit.quickcheck.generator.java.util.ArrayListGenerator;
import com.pholser.junit.quickcheck.generator.java.util.HashMapGenerator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import io.getstream.core.models.Activity;
import io.getstream.core.models.FeedID;
import java.util.Date;

public class ActivityGenerator extends Generator<Activity> {
  private static final class FeedIDGenerator extends Generator<FeedID> {
    public FeedIDGenerator() {
      super(FeedID.class);
    }

    @Override
    public FeedID generate(SourceOfRandomness r, GenerationStatus status) {
      StringGenerator stringGen = new StringGenerator();
      String slug = stringGen.generate(r, status);
      while (slug.contains(":")) {
        slug = stringGen.generate(r, status);
      }
      String userID = stringGen.generate(r, status);
      while (userID.contains(":")) {
        userID = stringGen.generate(r, status);
      }
      return new FeedID(slug, userID);
    }
  }

  private static final class DateGenerator extends Generator<Date> {
    private Date min = new Date(Integer.MIN_VALUE);
    private Date max = new Date(8099, 0, 1);

    public DateGenerator() {
      super(Date.class);
    }

    @Override
    public Date generate(SourceOfRandomness random, GenerationStatus status) {
      return new Date(random.nextLong(min.getTime(), max.getTime()));
    }
  }

  public ActivityGenerator() {
    super(Activity.class);
  }

  @Override
  public Activity generate(SourceOfRandomness r, GenerationStatus status) {
    DateGenerator dateGen = new DateGenerator();
    DoubleGenerator doubleGen = new DoubleGenerator();
    StringGenerator stringGen = new StringGenerator();
    FeedIDGenerator feedIDGen = new FeedIDGenerator();
    ArrayListGenerator listGen = new ArrayListGenerator();
    listGen.addComponentGenerators(ImmutableList.of(feedIDGen));
    HashMapGenerator mapGen = new HashMapGenerator();
    mapGen.addComponentGenerators(ImmutableList.of(stringGen, stringGen));

    Activity.Builder builder =
        Activity.builder()
            .actor(stringGen.generate(r, status))
            .verb(stringGen.generate(r, status))
            .object(stringGen.generate(r, status));
    if (r.nextBoolean()) {
      builder.id(stringGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.foreignID(stringGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.target(stringGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.time(dateGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.origin(stringGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.to(listGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.score(doubleGen.generate(r, status));
    }
    if (r.nextBoolean()) {
      builder.extra(mapGen.generate(r, status));
    }

    return builder.build();
  }
}

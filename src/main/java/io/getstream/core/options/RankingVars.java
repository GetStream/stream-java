package io.getstream.core.options;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.getstream.core.http.Request;
import java8.util.concurrent.CompletionException;

import java.io.IOException;
import java.util.Map;

public final class RankingVars implements RequestOption{

    private final String rankingVarsJSON;

    public RankingVars(Map<String, Object> externalVars) {
        String rankingVarsJSON;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            rankingVarsJSON = objectMapper.writeValueAsString(externalVars);
        }
        catch (IOException e){
            throw new CompletionException(e);
        }

        this.rankingVarsJSON = rankingVarsJSON;
    }

    public String getRankingVars() {
        return rankingVarsJSON;
    }

    @Override
    public void apply(Request.Builder builder) {
        builder.addQueryParameter("ranking_vars", rankingVarsJSON);
    }
}


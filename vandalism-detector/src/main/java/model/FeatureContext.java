package model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import wikixmlsplit.api.Matching;
import wikixmlsplit.datastructures.MyPageType;
import wikixmlsplit.datastructures.MyRevisionType;

import java.util.List;

@Value
@Builder
@RequiredArgsConstructor(staticName = "with")
public class FeatureContext {

    private final MyPageType page;
    private final List<MyRevisionType> previousRevisions;
    private final Matching matching;
}
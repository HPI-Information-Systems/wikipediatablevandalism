package model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import wikixmlsplit.datastructures.MyRevisionType;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor(staticName = "with")
public class FeatureContext {

    private final List<MyRevisionType> previousRevisions;
}
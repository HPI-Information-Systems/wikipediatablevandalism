from enum import Enum

import pandas as pd


class Tags(Enum):
    BLANKING = 1
    NONSENSE = 2
    QUALITY_ISSUE = 3
    PERSONAL_STORY = 4
    FALSE_FACT = 5
    SYNTAX = 6
    MERGE_CONFLICT = 7
    TEMPLATE = 8
    EDIT_WARS = 9
    SEO = 10
    INTENTION = 11
    CONSTRUCTIVE = 12


def mark_tags_as_vandalism(df: pd.DataFrame, tags: [Tags]):
    tag_ids = map(lambda tag: tag.value, tags)
    df['is_vandalism'] = df.tag_id.isin(tag_ids)
    return df

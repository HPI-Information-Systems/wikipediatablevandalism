def undersample(y_fold, weight_no_vandalism, weight_vandalism):
    train_counts = Counter(y_fold)
    no_vandalism_count = train_counts[0]
    vandalism_count = train_counts[1]
    sampled_no_vandalism_count = int(vandalism_count * (weight_no_vandalism / weight_vandalism))
    return {0: min(no_vandalism_count, sampled_no_vandalism_count), 1:vandalism_count}

def undersample_1_1(y_fold):
    return undersample(y_fold, 1, 1)

def undersample_15_1(y_fold):
    return undersample(y_fold, 1.5, 1)

def undersample_2_1(y_fold):
    return undersample(y_fold, 2, 1)

def undersample_25_1(y_fold):
    return undersample(y_fold, 2.5, 1)

def undersample_3_1(y_fold):
    return undersample(y_fold, 3, 1)

def undersample_5_1(y_fold):
    return undersample(y_fold, 5, 1)

def undersample_10_1(y_fold):
    return undersample(y_fold, 5, 1)

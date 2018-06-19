#! /usr/bin/env python
# -*- encoding: utf-8 -*-

import numpy as np
import pandas as pd
import sys

"""
Draft for splitting CSV files in chunks of equal size.

Input (first argument): filename (not path) of the CSV file to split
Output: 3 partitions of the input file, evenly split, suffix "_0", "_1", etc.
"""

filename = sys.argv[1]
f = pd.read_csv(filename, delimiter='|')
chunks = np.array_split(f, 3)


def to_output_filename(filename, nr):
	name, extension = filename.rsplit(".", 1)
	return "{}_{}.{}".format(name, nr, extension)


for index, chunk in enumerate(chunks):
	chunk.to_csv(to_output_filename(filename, index), 
		sep='|',
		index=False,
		columns=['id', 'page_id', 'created_at', 'table_count', 'changed_tables'],
		header=None)


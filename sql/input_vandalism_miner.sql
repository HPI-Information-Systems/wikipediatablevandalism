-- Select random from modified tables as input to the vandalism miner
\copy (select page_id, id from allmodifiedtables order by random() limit 10000) to 'input_vandalism_miner.csv' csv;
-- Select random from modified tables as input to the vandalism miner
\copy (select page_id, id from allmodifiedtables order by random() limit 10000) to 'input_vandalism_miner.csv' csv;


CREATE TABLE IF NOT EXISTS crypto_record (
                               id varchar(255) not null,
                               price_date timestamp not null,
                               symbol varchar(10) not null,
                               price decimal not null,
                               month int not null,
                               year int not null,
                               PRIMARY KEY (id)
                           );

CREATE INDEX idx_crypto_record_symb_mon_yr ON crypto_record(symbol, month, year);
CREATE INDEX idx_crypto_record_price_date ON crypto_record(price_date);

CREATE TABLE IF NOT EXISTS crypto_symbol (
                               symbol varchar(10) not null,
                               PRIMARY KEY (symbol)
                           );

CREATE TABLE IF NOT EXISTS crypto_statistics (
    symbol VARCHAR(10),
    oldest_value DECIMAL,
    newest_value DECIMAL,
    min_value DECIMAL,
    max_value DECIMAL,
    month INT,
    year INT,
    PRIMARY KEY (symbol, month, year)
);

CREATE INDEX idx_crypto_statistics_sym_yr_mn ON crypto_statistics(symbol, year, month);

INSERT INTO public.crypto_symbol (symbol) VALUES('LTC');
INSERT INTO public.crypto_symbol (symbol) VALUES('BTC');
INSERT INTO public.crypto_symbol (symbol) VALUES('DOGE');
INSERT INTO public.crypto_symbol (symbol) VALUES('ETH');
INSERT INTO public.crypto_symbol (symbol) VALUES('XRP');

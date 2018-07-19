package kr.doublechain.basic.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by 전성국 on 2018-05-11.
 */
@Data
@Getter @Setter
public class BlockVo {
//    private String hash;
//    private String miner;
//    private Long confirmations;
//    private Long size;
//    private Long height;
//    private Long version;
//    private String merkleroot;
//    private ArrayList<TransactionVo> tx;
//    private Long time;
//    private Long nonce;
//    private String bits;
//    private Double difficulty;
//    private String chainwork;
//    private String previousblockhash;
//    private String nextblockhash;
    private String hash;
    private Long version;
    private String hashMerkleRoot;
    private Long nTime;
    private String nBits;
    private Long nNonce;
    private Long height;
    private String chainWork;
    private Long valueIn;
    private Long valueOut;
    private Long satoshis;
    private Long seconds;
    private BigInteger totalSs;
    private Long blockNumTx;
    private ArrayList<String> tx;
    private Long valueDestroyed;
    private Long searchBlockId;


    private Long blockId;
    private String hashPrev;
    private Long prevBlockId;


}
/*
CREATE TABLE block (
    block_id      NUMERIC(14) NOT NULL PRIMARY KEY,             block_id
    block_hash    BINARY(32)  UNIQUE NOT NULL,                  block_hash
    block_version NUMERIC(10),                                  block_version
    block_hashMerkleRoot BINARY(32),                            block_hashMerkleRoot
    block_nTime   NUMERIC(20),                                  block_nTime
    block_nBits   NUMERIC(10),                                  block_nBits
    block_nNonce  NUMERIC(10),                                  block_nNonce
    block_height  NUMERIC(14) NULL,                             block_height
    prev_block_id NUMERIC(14) NULL,                             prev_block_id
    search_block_id NUMERIC(14) NULL,                           search_block_id
    block_chain_work BINARY(""" + str(WORK_BITS / 8) + """),    block_chain_work
    block_value_in NUMERIC(30) NULL,                            block_value_in
    block_value_out NUMERIC(30),                                block_value_out
    block_total_satoshis NUMERIC(26) NULL,                      block_total_satoshis
    block_total_seconds NUMERIC(20) NULL,                       block_total_seconds
    block_satoshi_seconds NUMERIC(28) NULL,
    block_total_ss NUMERIC(28) NULL,                            block_total_ss
    block_num_tx  NUMERIC(10) NOT NULL,                         block_num_tx
    block_ss_destroyed NUMERIC(28) NULL,
    FOREIGN KEY (prev_block_id)
        REFERENCES block (block_id),
    FOREIGN KEY (search_block_id)
        REFERENCES block (block_id)
)

"""CREATE TABLE chain (
    chain_id    NUMERIC(10) NOT NULL PRIMARY KEY,
    chain_name  VARCHAR(100) UNIQUE NOT NULL,
    chain_code3 VARCHAR(5)  NULL,
    chain_address_version VARBINARY(100) NOT NULL,
    chain_script_addr_vers VARBINARY(100) NULL,
    chain_address_checksum VARBINARY(100) NULL,
    chain_magic BINARY(4)     NULL,
    chain_policy VARCHAR(255) NOT NULL,
    chain_decimals NUMERIC(2) NULL,
    chain_last_block_id NUMERIC(14) NULL,
    chain_protocol_version NUMERIC(10) NOT NULL,
    FOREIGN KEY (chain_last_block_id)
        REFERENCES block (block_id)
)""",

"""CREATE TABLE chain_candidate (
    chain_id      NUMERIC(10) NOT NULL,
    block_id      NUMERIC(14) NOT NULL,
    in_longest    NUMERIC(1),
    block_height  NUMERIC(14),
    PRIMARY KEY (chain_id, block_id),
    FOREIGN KEY (block_id) REFERENCES block (block_id)
)""",

"""CREATE INDEX x_cc_block ON chain_candidate (block_id)""",

"""CREATE INDEX x_cc_chain_block_height

"""CREATE INDEX x_cc_block_height ON chain_candidate (block_height)""",

"""CREATE TABLE orphan_block (
    block_id      NUMERIC(14) NOT NULL PRIMARY KEY,
    block_hashPrev BINARY(32) NOT NULL,
    FOREIGN KEY (block_id) REFERENCES block (block_id)
)""",

"""CREATE INDEX x_orphan_block_hashPrev ON orphan_block (block_hashPrev)""",

"""CREATE TABLE block_next (
    block_id      NUMERIC(14) NOT NULL,
    next_block_id NUMERIC(14) NOT NULL,
    PRIMARY KEY (block_id, next_block_id),
    FOREIGN KEY (block_id) REFERENCES block (block_id),
    FOREIGN KEY (next_block_id) REFERENCES block (block_id)
)""",

"""CREATE TABLE tx (
    tx_id         NUMERIC(26) NOT NULL PRIMARY KEY,
    tx_hash       BINARY(32)  UNIQUE NOT NULL,
    tx_version    NUMERIC(10),
    tx_lockTime   NUMERIC(10),
    tx_size       NUMERIC(10)
)""",

"""CREATE TABLE block_tx (
    block_id      NUMERIC(14) NOT NULL,
    tx_id         NUMERIC(26) NOT NULL,
    tx_pos        NUMERIC(10) NOT NULL,
    PRIMARY KEY (block_id, tx_id),
    UNIQUE (block_id, tx_pos),
    FOREIGN KEY (block_id)
        REFERENCES block (block_id),
    FOREIGN KEY (tx_id)
        REFERENCES tx (tx_id)
)""",

"""CREATE INDEX x_block_tx_tx ON block_tx (tx_id)""",

"""CREATE TABLE pubkey (
    pubkey_id     NUMERIC(26) NOT NULL PRIMARY KEY,
    pubkey_hash   BINARY(20)  UNIQUE NOT NULL,
    pubkey        VARBINARY(""" + str(MAX_PUBKEY) + """) NULL,
    pubkey_flags  NUMERIC(32) NULL
)""",

"""CREATE TABLE multisig_pubkey (
    multisig_id   NUMERIC(26) NOT NULL,
    pubkey_id     NUMERIC(26) NOT NULL,
    PRIMARY KEY (multisig_id, pubkey_id),
    FOREIGN KEY (multisig_id) REFERENCES pubkey (pubkey_id),
    FOREIGN KEY (pubkey_id) REFERENCES pubkey (pubkey_id)
)""",

"""CREATE INDEX x_multisig_pubkey_pubkey ON multisig_pubkey (pubkey_id)""",

"""CREATE TABLE txout (
    txout_id      NUMERIC(26) NOT NULL PRIMARY KEY,
    tx_id         NUMERIC(26) NOT NULL,
    txout_pos     NUMERIC(10) NOT NULL,
    txout_value   NUMERIC(30) NOT NULL,
    txout_scriptPubKey VARBINARY(""" + str(MAX_SCRIPT) + """),
    pubkey_id     NUMERIC(26),
    UNIQUE (tx_id, txout_pos),
    FOREIGN KEY (pubkey_id)
        REFERENCES pubkey (pubkey_id)
)""",

"""CREATE INDEX x_txout_pubkey ON txout (pubkey_id)""",

"""CREATE TABLE txin (
    txin_id       NUMERIC(26) NOT NULL PRIMARY KEY,
    tx_id         NUMERIC(26) NOT NULL,
    txin_pos      NUMERIC(10) NOT NULL,
    txout_id      NUMERIC(26)""" + (""",
    txin_scriptSig VARBINARY(""" + str(MAX_SCRIPT) + """),
    txin_sequence NUMERIC(10)""" if store.keep_scriptsig else "") + """,
    UNIQUE (tx_id, txin_pos),
    FOREIGN KEY (tx_id)
        REFERENCES tx (tx_id)
)""",

"""CREATE INDEX x_txin_txout ON txin (txout_id)""",

"""CREATE TABLE unlinked_txin (
    txin_id       NUMERIC(26) NOT NULL PRIMARY KEY,
    txout_tx_hash BINARY(32)  NOT NULL,
    txout_pos     NUMERIC(10) NOT NULL,
    FOREIGN KEY (txin_id) REFERENCES txin (txin_id)
)""",

"""CREATE INDEX x_unlinked_txin_outpoint
    ON unlinked_txin (txout_tx_hash, txout_pos)""",

"""CREATE TABLE block_txin (
    block_id      NUMERIC(14) NOT NULL,
    txin_id       NUMERIC(26) NOT NULL,
    out_block_id  NUMERIC(14) NOT NULL,
    PRIMARY KEY (block_id, txin_id),
    FOREIGN KEY (block_id) REFERENCES block (block_id),
    FOREIGN KEY (txin_id) REFERENCES txin (txin_id),
    FOREIGN KEY (out_block_id) REFERENCES block (block_id)
)""",

"""CREATE TABLE asset (
    asset_id      NUMERIC(10) NOT NULL PRIMARY KEY,
    tx_id         NUMERIC(26) NOT NULL,
    chain_id      NUMERIC(10) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    multiplier    NUMERIC(10) NOT NULL,
    issue_qty     NUMERIC(30) NOT NULL,
    prefix        NUMERIC(10) NOT NULL,
    UNIQUE      (tx_id),
    FOREIGN KEY (tx_id) REFERENCES tx (tx_id),
    FOREIGN KEY (chain_id) REFERENCES chain (chain_id)
)""",

"""CREATE TABLE asset_txid (
    asset_id      NUMERIC(10) NOT NULL,
    tx_id         NUMERIC(26) NOT NULL,
    txout_pos     NUMERIC(10) NOT NULL,
    UNIQUE (asset_id, tx_id, txout_pos),
    FOREIGN KEY (tx_id) REFERENCES tx (tx_id),
    FOREIGN KEY (asset_id) REFERENCES asset (asset_id)
)""",

"""CREATE TABLE asset_address_balance (
    asset_id      NUMERIC(10) NOT NULL,
    pubkey_id     NUMERIC(26) NOT NULL,
    balance       NUMERIC(30) NOT NULL,
    PRIMARY KEY (asset_id, pubkey_id),
    FOREIGN KEY (asset_id) REFERENCES asset (asset_id),
    FOREIGN KEY (pubkey_id) REFERENCES pubkey (pubkey_id)
)""",

        b['value_in'] = 0
        b['value_out'] = 0
        b['value_destroyed'] = 0
        tx_hash_array = []





 */
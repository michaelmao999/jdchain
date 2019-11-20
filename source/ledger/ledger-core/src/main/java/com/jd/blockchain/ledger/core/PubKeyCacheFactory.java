package com.jd.blockchain.ledger.core;

import com.jd.blockchain.crypto.PubKey;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.BlockchainIdentityData;

import java.util.HashMap;
import java.util.Map;

public class PubKeyCacheFactory {

    private static Map<PubKey, BlockchainIdentity> nodePubKeyCache = new HashMap<>();

    public static BlockchainIdentity getNodeIdentity(PubKey pubKey) {
        BlockchainIdentity identity = nodePubKeyCache.get(pubKey);
        if (identity == null) {
            identity = getNodeIdentityInter(pubKey);
        }
        return identity;
    }

    private static synchronized BlockchainIdentity getNodeIdentityInter(PubKey pubKey) {
        BlockchainIdentity identity = nodePubKeyCache.get(pubKey);
        if (identity == null) {
            BlockchainIdentity identity1 = new BlockchainIdentityData(pubKey);
            nodePubKeyCache.put(pubKey, identity1);
            identity = identity1;
        }
        return identity;
    }


}

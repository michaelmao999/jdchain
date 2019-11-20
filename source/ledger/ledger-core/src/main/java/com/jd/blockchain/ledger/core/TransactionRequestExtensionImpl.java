package com.jd.blockchain.ledger.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.DigitalSignature;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.utils.Bytes;

/**
 * 交易请求的扩展信息；
 * 
 * @author huanghaiquan
 *
 */
public class TransactionRequestExtensionImpl implements TransactionRequestExtension {

	private TransactionRequest request;

	private Map<Bytes, Credential> endpointSignatures = new HashMap<>();

	private Bytes endpointAddress;
	private Credential endpointSignature;
	private boolean isSingleEndpointSignature;

	private Map<Bytes, Credential> nodeSignatures = new HashMap<>();

	private Bytes nodeAddress;
	private Credential nodeSignature;
	private boolean isSingleNodeSignature;


	public TransactionRequestExtensionImpl(TransactionRequest request) {
		this.request = request;
		resolveSigners();
	}

	private void resolveSigners() {
		if (request.getEndpointSignatures() != null) {
			DigitalSignature[] endpointSignatureList = request.getEndpointSignatures();
			isSingleEndpointSignature = endpointSignatureList.length == 1;
			for (DigitalSignature signature : endpointSignatureList) {
				if (isSingleEndpointSignature) {
					BlockchainIdentity identity = PubKeyCacheFactory.getNodeIdentity(signature.getPubKey());
					endpointSignature = new Credential(identity, signature);
					endpointAddress = identity.getAddress();
					endpointSignatures.put(endpointAddress, endpointSignature);
				} else {
					Credential cred = new Credential(signature);
					endpointSignatures.put(cred.getIdentity().getAddress(), cred);
				}
			}
		}
		if (request.getEndpointSignatures() != null) {
			DigitalSignature[] nodeSignatureList = request.getNodeSignatures();
			isSingleNodeSignature = nodeSignatureList.length == 1;
			for (DigitalSignature signature : nodeSignatureList) {
				if (isSingleNodeSignature) {
					BlockchainIdentity identity = PubKeyCacheFactory.getNodeIdentity(signature.getPubKey());
					nodeSignature = new Credential(identity, signature);
					nodeAddress = identity.getAddress();
					nodeSignatures.put(nodeAddress, nodeSignature);
				} else {
					Credential cred = new Credential(signature);
					nodeSignatures.put(cred.getIdentity().getAddress(), cred);
				}
			}
		}
	}

	@Override
	public boolean isSingleNodeSignature() {
		return isSingleNodeSignature;
	}

	@Override
	public Bytes getNodeAddress() {
		return nodeAddress;
	}

	@Override
	public boolean isSingleEndpointSignature() {
		return isSingleEndpointSignature;
	}

	@Override
	public Bytes getEndpointAddress() {
		return endpointAddress;
	}


	@Override
	public Set<Bytes> getEndpointAddresses() {
		return endpointSignatures.keySet();
	}

	@Override
	public Set<Bytes> getNodeAddresses() {
		return nodeSignatures.keySet();
	}

	@Override
	public Collection<Credential> getEndpoints() {
		return endpointSignatures.values();
	}

	@Override
	public Credential getEndpointSignature() {
		return endpointSignature;
	}


	@Override
	public Collection<Credential> getNodes() {
		return nodeSignatures.values();
	}

	@Override
	public Credential getNodeSignature() {
		return nodeSignature;
	}

	@Override
	public boolean containsEndpoint(Bytes address) {
		return endpointSignatures.containsKey(address);
	}

	@Override
	public boolean containsNode(Bytes address) {
		return nodeSignatures.containsKey(address);
	}

	@Override
	public DigitalSignature getEndpointSignature(Bytes address) {
		return endpointSignatures.get(address).getSignature();
	}

	@Override
	public DigitalSignature getNodeSignature(Bytes address) {
		return nodeSignatures.get(address).getSignature();
	}

	@Override
	public HashDigest getHash() {
		return request.getHash();
	}

	@Override
	public DigitalSignature[] getNodeSignatures() {
		return request.getNodeSignatures();
	}

	@Override
	public DigitalSignature[] getEndpointSignatures() {
		return request.getEndpointSignatures();
	}

	@Override
	public TransactionContent getTransactionContent() {
		return request.getTransactionContent();
	}

}

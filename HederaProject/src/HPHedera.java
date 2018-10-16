import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.account.HederaAccountUpdateValues;
import com.hedera.sdk.account.HederaClaim;
import com.hedera.sdk.common.HederaKey;
import com.hedera.sdk.common.HederaKey.KeyType;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import com.hedera.sdk.common.HederaTransactionID;
import com.hedera.sdk.common.HederaTransactionRecord;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;

public final class HPHedera {
	
	public static void main (String... arguments) throws Exception {
		final Logger logger = LoggerFactory.getLogger(DemoAccount.class);
		
		//DO NOT CHANGE THESE, CHANGE BELOW INSTEAD
		boolean create = false; //OK
    	boolean balance = false; //OK
    	boolean send = false; //NOK
    	boolean info = false; //OK
    	boolean update = false; //OK
    	boolean doAddClaim = false;//OK
		boolean getTXRecord = false;
		
		// setup a set of defaults for query and transactions
		HederaTransactionAndQueryDefaults txQueryDefaults = new HederaTransactionAndQueryDefaults();
		txQueryDefaults = ExampleUtilities.getTxQueryDefaults();
    	// new account objects
    	HederaAccount contractorAc = new HederaAccount();
    	HederaAccount citizenAc = new HederaAccount();
    	
    	// setup transaction/query defaults (durations, etc...)
    	contractorAc.txQueryDefaults = txQueryDefaults;
    	citizenAc.txQueryDefaults = txQueryDefaults;
    	
    	create = true;
    	balance = true;
    	//send = true;
    	//info = true;
    	//update = true;
//    	doAddClaim = true; -- not implemented ?
    	//getTXRecord = true;
    	
		// create an account
    	if (create) {
    		contractorAc.txQueryDefaults.generateRecord = getTXRecord;
	    	HederaCryptoKeyPair contractorAcKey = new HederaCryptoKeyPair(KeyType.ED25519);
	    	HederaCryptoKeyPair accountXferToKey = new HederaCryptoKeyPair(KeyType.ED25519);
			Thread.sleep(5000);

			contractorAc = AccountCreate.create(contractorAc, contractorAcKey, 100);
	    	Thread.sleep(3000);
	    	citizenAc = AccountCreate.create(citizenAc, accountXferToKey, 100);
	   
	    	if (contractorAc == null) {
    			logger.info("*******************************************");
    			logger.info("FIRST ACCOUNT CREATE FAILED");
    			logger.info("*******************************************");
    			throw new Exception("Account create failure");
	    	} else {
	    		if (getTXRecord) {
	    			  HederaTransactionID txID = contractorAc.hederaTransactionID;
	    			  HederaTransactionRecord txRecord = new HederaTransactionRecord(txID, 10, txQueryDefaults);
    			}
	    	}
    		contractorAc.txQueryDefaults.generateRecord = false;
	    	if (contractorAc != null) {
	    		// the paying account is now the new account
	    		txQueryDefaults.payingAccountID = contractorAc.getHederaAccountID();
	    		txQueryDefaults.payingKeyPair = contractorAcKey;

	    		// get balance for the account
	    		if (balance) {
	    			Thread.sleep(10000);
	    			AccountGetBalance.getBalance(contractorAc);
	    			System.out.println("This is a contractor balance!");
	    		}
	    	}
	    	
	    	if (citizenAc != null) {
	    		// the paying account is now the new account
	    		txQueryDefaults.payingAccountID = citizenAc.getHederaAccountID();
	    		txQueryDefaults.payingKeyPair = accountXferToKey;

	    		// get balance for the account
	    		if (balance) {
	    			Thread.sleep(2000);
	    			AccountGetBalance.getBalance(citizenAc);
	    			System.out.println("This is a Citizen balance!");
	    		}
	    	}

//	        if (send) {	    			
//	        	Thread.sleep(5000);
//		    	citizenAc = AccountCreate.create(citizenAc, accountXferToKey, 100);
//		    	if (citizenAc == null) {
//	    			logger.info("*******************************************");
//	    			logger.info("SECOND ACCOUNT CREATE FAILED");
//	    			logger.info("*******************************************");
//	    			throw new Exception("Account create failure");
//		    	}
//	    	}
	    	
	    	if (contractorAc != null) {

		        // get balance for the account
		    	if (balance) {
	    			Thread.sleep(5000);
		    		AccountGetBalance.getBalance(contractorAc);
		    	}
		
				// send some crypto
//		    	if (send) {
//		    		AccountSend.send(ContractorAc, citizenAc, 100);
//		    	}
				// get balance for the account
		    	if (balance) {
		    		AccountGetBalance.getBalance(contractorAc);
		    	}
				// get account info
		    	if (info) {
		    		AccountGetInfo.getInfo(contractorAc);
		    	}
		
				// update the account
		    	if (update) {
		    		// setup an object to contain values to update
		    		HederaAccountUpdateValues updates = new HederaAccountUpdateValues();
		    		
		    		// create a new key
		    	    HederaCryptoKeyPair ed25519Key = new HederaCryptoKeyPair(KeyType.ED25519);
		    	    			
		    	    // set the new key for the account
		    		updates.newKey = ed25519Key;
		    		// new proxy account details
		    		updates.proxyAccountShardNum = 0;
		    		updates.proxyAccountRealmNum = 0;
		    		updates.proxyAccountAccountNum = 1;
		    		// new proxy fraction
		    		updates.proxyFraction = 2;
		    		// new threshold for sending
		    		updates.sendRecordThreshold = 4000;
		    		// new threshold for receiving
		    		updates.receiveRecordThreshold = 3000;
		    		// new auto renew period
		    		updates.autoRenewPeriodSeconds = 10;
		    		updates.autoRenewPeriosNanos = 20;
		    		// new expiration time
		    		updates.expirationTimeSeconds = 200;
		    		updates.expirationTimeNanos = 100;
		    		
		    		contractorAc = AccountUpdate.update(contractorAc, updates);
		    		if (contractorAc != null) {
		    			AccountGetInfo.getInfo(contractorAc);
		    		} else {
		    			logger.info("*******************************************");
		    			logger.info("ACCOUNT UPDATE FAILED - account is now null");
		    			logger.info("*******************************************");
		    		}
		    	}
		
		    	if ((contractorAc != null) && (doAddClaim)) {
			    	HederaCryptoKeyPair claimKeyPair = new HederaCryptoKeyPair(KeyType.ED25519);
			        HederaKey claimKey = new HederaKey(claimKeyPair.getKeyType(), claimKeyPair.getPublicKey());
			
					// Create a new claim object
					HederaClaim claim;
					claim = new HederaClaim(contractorAc.shardNum, contractorAc.realmNum, contractorAc.accountNum, "ClaimHash".getBytes("UTF-8"));
					// add a key to the claim
					claim.addKey(claimKey);
			        // add a claim
			        if (AccountAddClaim.addClaim(contractorAc,claim, claimKeyPair)) {
			        }
		    	} else if (contractorAc == null) {
	    			logger.info("*******************************************");
	    			logger.info("ACCOUNT object is null, skipping claim tests");
	    			logger.info("*******************************************");
		    	}
	    	}
	    	
	    	
	    	/* Causing the transaction from the Administration to the citizen */
	    	
	    	if((contractorAc!= null && contractorAc instanceof HederaAccount) && (citizenAc!= null && citizenAc instanceof HederaAccount)) {
	    		int xDenomination = 50;
	    		
	    		/* Checking the Balance info */
//	    		if(AccountGetBalance.getBalance(citizenAc) < xDenomination)
	    		long retVal = citizenAc.getBalance();
	    		System.out.println("The Balance of the citizenAc is: " + retVal);
	    		
	    		AccountSend.send(citizenAc, contractorAc, xDenomination);
	    		System.out.println("Denomination Transfered!");
	    	}
	    	
	    	/* Getting information of the account */
	    	
    	}	    	
	}
}
//package com.hedera.hellofuture;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.account.HederaAccountCreateDefaults;
import com.hedera.sdk.common.HederaAccountID;
import com.hedera.sdk.common.HederaDuration;
import com.hedera.sdk.common.HederaPrecheckResult;
import com.hedera.sdk.common.HederaKey.KeyType;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import com.hedera.sdk.common.HederaTransactionReceipt;
import com.hedera.sdk.common.HederaTransactionStatus;
import com.hedera.sdk.common.Utilities;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.node.HederaNode;
import com.hedera.sdk.transaction.HederaTransactionResult;

public final class myHedera
{

	public static void main (String[] args) throws Exception 
	{


		// node details
//		String nodeAddress = "localhost";
//		int nodePort = 50211;
		String nodeAddress = "testnet14.hedera.com";
		int nodePort = 80;
		
		// these are loaded from config.properties below
		long nodeAccountShard = 0;
		long nodeAccountRealm = 0;
		long nodeAccountNum = 2;

		// contractor account details
		long CNAccountShard = 0;
		long CNAccountRealm = 0;
		long CNAccountNum = 1001;

		// citizen1 account details
		long CZ1AccountShard = 0;
		long CZ1AccountRealm = 0;
		long CZ1AccountNum = 0;

		// citizen2 account details
		long CZ2AccountShard = 0;
		long CZ2AccountRealm = 0;
		long CZ2AccountNum = 0;

		String Persona;//REMEBER TO UPDATE THIS,THIS IS BEING PASSES TO THE ACC CREATE FUNCTION


        // you public and private keys
		String pubKey = "302a300506032b6570032100c71fc098155a8444c302542898b5bb24bffd937c2e3fea21dc36756023b2b25e";
        String privKey = "302e020100300506032b6570042204208f8ac828bb88582daddc0ddea72923cbabf86d87238369a69b1e27f82bcec832";
		
		// load application properties
		Properties applicationProperties = new Properties();
		InputStream propertiesInputStream = null;

		propertiesInputStream = new FileInputStream("config.properties");
		// load a properties file
		applicationProperties.load(propertiesInputStream);
		
		
		try {
			propertiesInputStream = new FileInputStream("config.properties");
			// load a properties file
			applicationProperties.load(propertiesInputStream);
			// get the node's account values
			nodeAccountShard = Long.parseLong(applicationProperties.getProperty("nodeAccountShard"));
			nodeAccountRealm = Long.parseLong(applicationProperties.getProperty("nodeAccountRealm"));
			nodeAccountNum = Long.parseLong(applicationProperties.getProperty("nodeAccountNum"));
			System.out.println("Node Ac Shard"+nodeAccountShard);
			System.out.println("Node Ac Realm"+nodeAccountRealm);
			System.out.println("Node Ac Num"+nodeAccountNum);
			// get my public/private keys
			pubKey = applicationProperties.getProperty("pubkey");
			privKey = applicationProperties.getProperty("privkey");

			// get Contactor account details
			CNAccountShard = Long.parseLong(applicationProperties.getProperty("CNAccountShard"));
			CNAccountRealm = Long.parseLong(applicationProperties.getProperty("CNAccountRealm"));
			CNAccountNum = Long.parseLong(applicationProperties.getProperty("CNAccountNum"));

			
			

		} catch (IOException ex) {
			ex.printStackTrace();         

			}


		//For the contractor
		HederaTransactionAndQueryDefaults CNtxQueryDefaults = new HederaTransactionAndQueryDefaults();
		
		Persona="MLA";

		CNtxQueryDefaults.memo = "I am contractor: Roadlife = 4 years";

		CNtxQueryDefaults.payingAccountID = new HederaAccountID(CNAccountShard,CNAccountRealm,CNAccountNum);
		CNtxQueryDefaults.payingKeyPair = new HederaCryptoKeyPair(KeyType.ED25519, pubKey, privKey);
		// define the valid duration for the transactions (seconds, nanos)
		CNtxQueryDefaults.transactionValidDuration = new HederaDuration(120, 0);
		// instantiate a contractor account object
		HederaAccount ContractorAccount = new HederaAccount();
		//transfering contents from populated object
		ContractorAccount.txQueryDefaults = CNtxQueryDefaults;
		// create a new key for new contactor account
		HederaCryptoKeyPair ContractorAccountKey = new HederaCryptoKeyPair(KeyType.ED25519);

		// now, setup default for account creation 
		HederaAccountCreateDefaults defaults = new HederaAccountCreateDefaults();
		defaults.autoRenewPeriodSeconds = 86400;
		defaults.autoRenewPeriodNanos = 0;
		HederaTransactionResult createResult=null;
		try {

			// send create account transaction
			long shardToCreateIn = 0;
			long realmToCreateIn = 0;
			long startingBalance = 10000;
			// let's create the account
			 createResult = ContractorAccount.create(shardToCreateIn,realmToCreateIn
			, ContractorAccountKey.getPublicKey()
			, ContractorAccountKey.getKeyType()
			, startingBalance
			, defaults);

			defaults.autoRenewPeriodSeconds = 86400;
			defaults.autoRenewPeriodNanos = 0;
			
	}
		catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	

				





//		//			//			///			/	//	/	/	/	////	///	/	///	//	/	/	///	//	/	//	



		//For the Citizen1
		HederaTransactionAndQueryDefaults CZ1txQueryDefaults = new HederaTransactionAndQueryDefaults();
		
		Persona="Citizen";

		CZ1txQueryDefaults.memo = "I am Citizen-1";

		CZ1txQueryDefaults.payingAccountID = new HederaAccountID(CZ1AccountShard,CZ1AccountRealm,CZ1AccountNum);
		CZ1txQueryDefaults.payingKeyPair = new HederaCryptoKeyPair(KeyType.ED25519, pubKey, privKey);
		// define the valid duration for the transactions (seconds, nanos)
		CZ1txQueryDefaults.transactionValidDuration = new HederaDuration(120, 0);
		// instantiate a contractor account object
		HederaAccount Citizen1Account = new HederaAccount();
		//transfering contents from populated object
		Citizen1Account.txQueryDefaults = CZ1txQueryDefaults;







//		//			//			///			/	//	/	/	/	////	///	/	///	//	/	/	///	//	/	//	



		//For the Citizen2
		HederaTransactionAndQueryDefaults CZ2txQueryDefaults = new HederaTransactionAndQueryDefaults();
		
		Persona="Citizen";

		CZ2txQueryDefaults.memo = "I am Citizen-2";

		CZ2txQueryDefaults.payingAccountID = new HederaAccountID(CZ2AccountShard,CZ2AccountRealm,CZ2AccountNum);
		CZ2txQueryDefaults.payingKeyPair = new HederaCryptoKeyPair(KeyType.ED25519, pubKey, privKey);
		// define the valid duration for the transactions (seconds, nanos)
		CZ2txQueryDefaults.transactionValidDuration = new HederaDuration(120, 0);
		// instantiate a contractor account object
		HederaAccount Citizen2Account = new HederaAccount();
		//transfering contents from populated object
		Citizen2Account.txQueryDefaults = CZ2txQueryDefaults;



		

		
		if (createResult.getPrecheckResult() == HederaPrecheckResult.OK) 
			
			{
			HederaTransactionReceipt receipt = Utilities.getReceipt(ContractorAccount.hederaTransactionID, ContractorAccount.txQueryDefaults.node);

			// was that successful ?
			Thread.sleep(1000);

			if (receipt.transactionStatus == HederaTransactionStatus.SUCCESS) {
				// yes, get the new account number from the receipt
				ContractorAccount.accountNum = receipt.accountID.accountNum;
				// and print it out
				System.out.println(String.format("===>Your Contractor account number is %d", ContractorAccount.accountNum));

				// get balance
				ContractorAccount.txQueryDefaults.payingAccountID = ContractorAccount.getHederaAccountID();
				ContractorAccount.txQueryDefaults.payingKeyPair = ContractorAccountKey;
				Thread.sleep(1000);

				ContractorAccount.getBalance();
				
				//CHECK ToAccountID 
				HederaAccountID toAccountID = new HederaAccountID(CZ1AccountShard, CZ1AccountRealm, CZ1AccountNum);
				ContractorAccount.send(toAccountID, 100);

				Thread.sleep(1000);
					
				ContractorAccount.getBalance();

				Thread.sleep(1000);

				Citizen1Account.getBalance();

				Citizen2Account.getBalance();

			}
		}


	}
}


		
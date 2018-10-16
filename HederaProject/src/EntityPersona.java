import java.io.InputStream;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import com.hedera.sdk.account.HederaAccount;
import com.hedera.sdk.common.HederaPrecheckResult;
import com.hedera.sdk.common.HederaTransactionAndQueryDefaults;
import com.hedera.sdk.common.HederaTransactionReceipt;
import com.hedera.sdk.common.HederaTransactionStatus;
import com.hedera.sdk.common.Utilities;
import com.hedera.sdk.cryptography.HederaCryptoKeyPair;
import com.hedera.sdk.transaction.HederaTransactionResult;

public class EntityPersona {
	
	public static String persona = ""; 
	
	public static HederaAccount create(HederaAccount account, HederaCryptoKeyPair newAccountKey, long initialBalance, String persona) throws Exception {
		long shardNum = 0;
		long realmNum = 0;
		
		HederaTransactionResult createResult = account.create(shardNum, realmNum, newAccountKey.getPublicKey(), newAccountKey.getKeyType(), initialBalance, null);
		if(createResult.getPrecheckResult() == HederaPrecheckResult.OK) {
			HederaTransactionReceipt reciept = Utilities.getReceipt(account.hederaTransactionID, account.txQueryDefaults.node);
			if(reciept.transactionStatus == HederaTransactionStatus.SUCCESS) {
				account.accountNum = reciept.accountID.accountNum;
				System.out.println("Your new Account is ==> " + account.accountNum);
			} else {
				System.out.println("Transaction is not successful: " + reciept.transactionStatus.name());
				return null;
			}
		} else {
			System.out.println("getPrecheckStatus not OK: " + createResult.getPrecheckResult().name());
			return null;
		}
		return account;
	}

	public static String getPersona() {
		return persona;
	}

	public static void setPersona(String persona) {
		EntityPersona.persona = persona;
	}
	
	
}

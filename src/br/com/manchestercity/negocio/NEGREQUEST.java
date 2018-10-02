
package br.com.manchestercity.negocio;

import java.sql.Connection;
import java.sql.ResultSet;

import br.com.manchestercity.automacao.Database;
import br.com.manchestercity.automacao.User;
import br.com.manchestercity.automacao.ValidateReturn;
import br.com.manchestercity.sql.SQLREQUEST;

public class NEGREQUEST {
	Database lcdb;
	User user;

	String mFIRSTNAME;
	String mLASTNAME;
	String mDATEBIRTH;
	String mNATONALITY;
	String mGENDER;
	String mCOUNTRY;
	String mCITY;
	String mPOSTCODE;
	String mEMAIL;
	String mPHONE;
	String mISCITYZEN;
	String mSUPPORTERNUMBER;
	String mLALLOWCONTACT;
	String mTRDPARTYCONTACT;
	String mOPENTEXT;
	String mSTATUS;
	String mCD_REQUEST;
	String mORIGEM;

	public NEGREQUEST(User user) {
		this.lcdb = new Database(user);
		this.user = user;

		this.nullFields();
	}

	public void nullFields() {
		this.mCD_REQUEST = null;
		this.mFIRSTNAME = null;
		this.mLASTNAME = null;
		this.mDATEBIRTH = null;
		this.mNATONALITY = null;
		this.mGENDER = null;
		this.mCOUNTRY = null;
		this.mCITY = null;
		this.mPOSTCODE = null;
		this.mEMAIL = null;
		this.mPHONE = null;
		this.mISCITYZEN = null;
		this.mSUPPORTERNUMBER = null;
		this.mLALLOWCONTACT = null;
		this.mTRDPARTYCONTACT = null;
		this.mOPENTEXT = null;
		this.mSTATUS = null;
		this.mORIGEM = null;
	}

	public void emptyFields() {
		this.mCD_REQUEST = "";
		this.mFIRSTNAME = "";
		this.mLASTNAME = "";
		this.mDATEBIRTH = "";
		this.mNATONALITY = "";
		this.mGENDER = "";
		this.mCOUNTRY = "";
		this.mCITY = "";
		this.mPOSTCODE = "";
		this.mEMAIL = "";
		this.mPHONE = "";
		this.mISCITYZEN = "";
		this.mSUPPORTERNUMBER = "";
		this.mLALLOWCONTACT = "";
		this.mTRDPARTYCONTACT = "";
		this.mOPENTEXT = "";
		this.mSTATUS = "";
		this.mORIGEM = "";
	}

	public String getORIGEM() {
		return mORIGEM;
	}

	public void setORIGEM(String mORIGEM) {
		this.mORIGEM = mORIGEM;
	}

	public void setFIRSTNAME(String mFIRSTNAME) {
		this.mFIRSTNAME = mFIRSTNAME;
	}

	public String getFIRSTNAME() {
		return mFIRSTNAME;
	}
	
	public void setCD_REQUEST(String mCD_REQUEST) {
		this.mCD_REQUEST = mCD_REQUEST;
	}

	public String getCD_REQUEST() {
		return mCD_REQUEST;
	}
	

	public void setLASTNAME(String mLASTNAME) {
		this.mLASTNAME = mLASTNAME;
	}

	public String getLASTNAME() {
		return mLASTNAME;
	}

	public void setDATEBIRTH(String mDATEBIRTH) {
		this.mDATEBIRTH = mDATEBIRTH;
	}

	public String getDATEBIRTH() {
		return mDATEBIRTH;
	}

	public void setNATONALITY(String mNATONALITY) {
		this.mNATONALITY = mNATONALITY;
	}

	public String getNATONALITY() {
		return mNATONALITY;
	}

	public void setGENDER(String mGENDER) {
		this.mGENDER = mGENDER;
	}

	public String getGENDER() {
		return mGENDER;
	}

	public void setCOUNTRY(String mCOUNTRY) {
		this.mCOUNTRY = mCOUNTRY;
	}

	public String getCOUNTRY() {
		return mCOUNTRY;
	}

	public void setCITY(String mCITY) {
		this.mCITY = mCITY;
	}

	public String getCITY() {
		return mCITY;
	}

	public void setPOSTCODE(String mPOSTCODE) {
		this.mPOSTCODE = mPOSTCODE;
	}

	public String getPOSTCODE() {
		return mPOSTCODE;
	}

	public void setEMAIL(String mEMAIL) {
		this.mEMAIL = mEMAIL;
	}

	public String getEMAIL() {
		return mEMAIL;
	}

	public void setPHONE(String mPHONE) {
		this.mPHONE = mPHONE;
	}

	public String getPHONE() {
		return mPHONE;
	}

	public void setISCITYZEN(String mISCITYZEN) {
		this.mISCITYZEN = mISCITYZEN;
	}

	public String getISCITYZEN() {
		return mISCITYZEN;
	}

	public void setSUPPORTERNUMBER(String mSUPPORTERNUMBER) {
		this.mSUPPORTERNUMBER = mSUPPORTERNUMBER;
	}

	public String getSUPPORTERNUMBER() {
		return mSUPPORTERNUMBER;
	}

	public void setLALLOWCONTACT(String mLALLOWCONTACT) {
		this.mLALLOWCONTACT = mLALLOWCONTACT;
	}

	public String getLALLOWCONTACT() {
		return mLALLOWCONTACT;
	}

	public void setTRDPARTYCONTACT(String mTRDPARTYCONTACT) {
		this.mTRDPARTYCONTACT = mTRDPARTYCONTACT;
	}

	public String getTRDPARTYCONTACT() {
		return mTRDPARTYCONTACT;
	}

	public void setOPENTEXT(String mOPENTEXT) {
		this.mOPENTEXT = mOPENTEXT;
	}

	public String getOPENTEXT() {
		return mOPENTEXT;
	}

	public void setSTATUS(String mSTATUS) {
		this.mSTATUS = mSTATUS;
	}

	public String getSTATUS() {
		return mSTATUS;
	}

	public long insert(Connection conn, StringBuffer errors) {
		try {
			return new SQLREQUEST(user).insert(lcdb, conn, errors, this.mCD_REQUEST, this.mFIRSTNAME, this.mLASTNAME, this.mDATEBIRTH, this.mNATONALITY, this.mGENDER, this.mCOUNTRY, this.mCITY, this.mPOSTCODE, this.mEMAIL, this.mPHONE, this.mISCITYZEN, this.mSUPPORTERNUMBER, this.mLALLOWCONTACT, this.mTRDPARTYCONTACT, this.mOPENTEXT, this.mSTATUS, this.mORIGEM);
		}
		catch (Exception e) {
			errors.append(e.getMessage());
		}
		return -1;
	}

	public boolean update(Connection conn, StringBuffer errors) {
		try {
			return new SQLREQUEST(user).update(conn, errors, this.mCD_REQUEST, this.mFIRSTNAME, this.mLASTNAME, this.mDATEBIRTH, this.mNATONALITY, this.mGENDER, this.mCOUNTRY, this.mCITY, this.mPOSTCODE, this.mEMAIL, this.mPHONE, this.mISCITYZEN, this.mSUPPORTERNUMBER, this.mLALLOWCONTACT, this.mTRDPARTYCONTACT, this.mOPENTEXT, this.mSTATUS, this.mORIGEM);
		}
		catch (Exception e) {
			errors.append(e.getMessage());
		}
		return false;
	}

	public boolean delete(Connection conn, StringBuffer errors) {
		try {
			return new SQLREQUEST(user).delete(conn, errors);
		}
		catch (Exception e) {
			errors.append(e.getMessage());
		}
		return false;
	}

	public ResultSet refresh(Connection conn, String pCD_REQUEST, String pFIRSTNAME, String pLASTNAME, String pDATEBIRTH, String pNATONALITY, String pGENDER, String pCOUNTRY, String pCITY, String pPOSTCODE, String pEMAIL, String pPHONE, String pISCITYZEN, String pSUPPORTERNUMBER, String pLALLOWCONTACT, String pTRDPARTYCONTACT, String pOPENTEXT, String pSTATUS, String pOrderBy) {
		try {
			return lcdb.openResultSet(new SQLREQUEST(user).refresh(pCD_REQUEST,pFIRSTNAME, pLASTNAME, pDATEBIRTH, pNATONALITY, pGENDER, pCOUNTRY, pCITY, pPOSTCODE, pEMAIL, pPHONE, pISCITYZEN, pSUPPORTERNUMBER, pLALLOWCONTACT, pTRDPARTYCONTACT, pOPENTEXT, pSTATUS, pOrderBy), conn);
		}
		catch (Exception e) {
			return null;
		}
	}

	public ValidateReturn validate(Connection conn) {
		ValidateReturn validate = new ValidateReturn();
		StringBuffer message = new StringBuffer();

		validate.setMessage(message.toString());
		
		return validate;
	}

}

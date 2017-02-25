package com.yd.persistence.repository.model;

/**
 * @author edys
 * @version 3.0.0, Feb 6, 2014
 * @since 3.0.0
 */
public interface ColumnConst {

    int SIZE_uuid = 36;
    int SIZE_name = 50;
    int SIZE_Account_name = 50;
    int SIZE_AccountGroup_name = 30;
    int SIZE_Docstore_reference = 50;
    int SIZE_Document_reference = 20;
    int SIZE_Document_label = 30;
    int SIZE_Document_transactionId = 40;
    int SIZE_DocumentSignature_signee_id = 50;
    int SIZE_DocumentSignature_signature = 128;

    int SIZE_DocumentType_name = SIZE_name;
    int SIZE_DocumentTypeSet_name = SIZE_name;
    int SIZE_Permission_name = 30;
    int SIZE_Role_name = SIZE_name;
    int SIZE_User_name = 50;
    int SIZE_XchangeNetwork_name = 20;
    int SIZE_XchangeNetwork_processName = 50;
    int SIZE_XchangeNetwork_repositoryName = 20;
    int SIZE_XchangeNetwork_resourceKey = 80;
    int SIZE_XchangeNetwork_resourceValue = 50;
    int SIZE_Adress_Name = 80;
    int SIZE_Adress_Shortname = 80;
    int SIZE_Adress_City = 35;
    int SIZE_Adress_Country = 40;
    int SIZE_Adress_PostalCode = 80;
    int SIZE_Adress_Street = 80;
    int SIZE_Adress_CountryISOCode = 3;
    int SIZE_Adress_LanguageISOCode = 3;
    int SIZE_ReportType_ScreenName = 150;
    int SIZE_ReportType_FileName = 150;
    int SIZE_ReportType_ReportCode = 10;
    int SIZE_ReportGenerateStatus_Status = 20;
    int SIZE_ReportGenerateStatus_FileName = 100;
    int SIZE_RejectReason = 20;
    int SIZE_Currency_ISO_Code = 3;

    String NAME_Account_name = "accountname";
    String NAME_AccountGroup_name = "groupname";
    String NAME_Docstore_reference = "reference";
    String NAME_Document_label = "label";
    String NAME_Role_admin = "admin";
    String NAME_User_name = "username";
    String NAME_BankDocstore = "bankdocstore";
    String NAME_CurrencyDefault = "currency";
    String NAME_FundingPercentageUpdateTime = "fundingPercentageUpdateTime";
    String NAME_InitialTransactionId = "initialTransactionId";
    String NAME_MaximumCreditLine = "maximumCreditLine";
    String NAME_MaximumCreditLineUpdateTime = "maximumCreditLineUpdateTime";
    String NAME_PartyDocstore = "partyDocstore";
    String NAME_PreviousTransactionId = "previousTransactionId";
    String NAME_FundingPercentage = "fundingPercentage";
    String NAME_CommissionPercentage = "commissionPercentage";
    String NAME_ApprovalLevel1 = "approvalLevel1";
    String NAME_ApprovalLevel2 = "approvalLevel2";
    String NAME_ApprovalLevel3 = "approvalLevel3";
    String NAME_ApprovalLevel4 = "approvalLevel4";
    String NAME_ApprovalLevel5 = "approvalLevel5";
    String NAME_DocumentUuid = "documentuuid";
    String NAME_TransactionAmount = "transactionAmount";
    String NAME_OutstandingCredit = "outstandingCredit";
    String NAME_DateCreated = "datecreated";
    String NAME_PrincipalDocstore = "principalDocstore";
    String NAME_ParentDocstore = "parentDocstore";
    String NAME_CreditType = "creditType";

    String REF_Account = "account";
    String REF_AccountGroup = "accgrp";
    String REF_Docstore = "docstore";
    String REF_DocumentType = "doctype";
    String REF_DocumentTypeSet = "doctypeset";
    String REF_Permission = "permission";
    String REF_Role = "role";
    String REF_User = "user";
    String REF_XchangeNetwork = "xchangenet";

}

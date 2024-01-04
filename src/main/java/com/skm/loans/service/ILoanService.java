package com.skm.loans.service;

import com.skm.loans.dto.LoansDto;

public interface ILoanService  {
    /**
     * @param mobileNumber
     */
    void createLoan(String mobileNumber);

    /**
     * @param mobileNumber
     * @return
     */
    LoansDto fetchLoan(String mobileNumber);

    /**
     * @param loansDto
     */
    boolean updateLoan(LoansDto loansDto);

    /**
     * @param mobileNumber
     */
    boolean deleteLoan(String mobileNumber);

}

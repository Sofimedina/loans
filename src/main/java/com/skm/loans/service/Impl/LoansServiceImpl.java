package com.skm.loans.service.Impl;

import com.skm.loans.constants.LoansConstants;
import com.skm.loans.dto.LoansDto;
import com.skm.loans.entity.Loans;
import com.skm.loans.exception.LoanAlreadyExistsException;
import com.skm.loans.exception.ResourceNotFoundException;
import com.skm.loans.mapper.LoansMapper;
import com.skm.loans.repository.LoansRepository;
import com.skm.loans.service.ILoanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoanService {

    private LoansRepository loansRepository;

    /**
     * @param mobileNumber
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoan= loansRepository.findByMobileNumber(mobileNumber);
        if (optionalLoan.isPresent()){
            throw new LoanAlreadyExistsException("Loan exist for number:"+mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    private Loans createNewLoan(String mobileNumber){
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {

        Loans loan=loansRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Loan","Mobile Number",mobileNumber));
        return LoansMapper.mapToLoanDto(loan,new LoansDto());
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        boolean isUpdated=false;

        Loans loan=loansRepository
                .findByMobileNumber(loansDto.getMobileNumber())
                .orElseThrow(()->new ResourceNotFoundException("Loan","Mobile Number",loansDto.getMobileNumber()));

        loansRepository.save(LoansMapper.mapToLoan(loansDto,loan));
        isUpdated=true;
        return isUpdated;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loan=loansRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Loan","Mobile Number",mobileNumber));
        loansRepository.delete(loan);

        return true;
    }
}

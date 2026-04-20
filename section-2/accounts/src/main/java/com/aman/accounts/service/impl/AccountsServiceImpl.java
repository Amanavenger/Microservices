package com.aman.accounts.service.impl;

import com.aman.accounts.constants.AccountsConstants;
import com.aman.accounts.dto.AccountsDto;
import com.aman.accounts.dto.CustomerDto;
import com.aman.accounts.entity.Accounts;
import com.aman.accounts.entity.Customer;
import com.aman.accounts.exception.CustomerAlreadyExist;
import com.aman.accounts.exception.ResourceNotFoundException;
import com.aman.accounts.mapper.AccountsMapper;
import com.aman.accounts.mapper.CustomerMapper;
import com.aman.accounts.repository.AccountsRepository;
import com.aman.accounts.repository.CustomerRepository;
import com.aman.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExist("Customer already registered with the given mobile number: " + customerDto.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customerDetails = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer",  "mobileNumber", mobileNumber)
        );

        Accounts accountsDetails =  accountsRepository.findByCustomerId(customerDetails.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account",  "customerId", customerDetails.getCustomerId().toString())
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customerDetails, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accountsDetails, new AccountsDto()));

        return customerDto;

    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean update = false;

        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    ()-> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();

            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer",  "customerId", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            update = true;
        }

        return update;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customerDetails = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer",  "mobileNumber", mobileNumber)
        );

        accountsRepository.deleteByCustomerId(customerDetails.getCustomerId());
        customerRepository.deleteById(customerDetails.getCustomerId());

        return true;
    }
}

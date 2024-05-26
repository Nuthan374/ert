import csv
import random
from faker import Faker
from datetime import datetime, timedelta

fake = Faker()

headers = [
    "customerId", "customerName", "creditScore", "income", "debtToIncomeRatio",
    "paymentHistory", "employmentYears", "loanAmount", "loanPurpose", "existingLoans",
    "propertyValue", "maritalStatus", "educationLevel", "industry", "riskCategory",
    "status", "additionalInfo", "recommendations", "birthDate", "age",
    "employmentType", "monthlyExpenses", "savingsBalance", "creditHistoryYears",
    "numberOfCreditCards", "creditCardUtilization", "hasBankruptcy", "bankruptcyYearsAgo",
    "hasForeclosure", "foreclosureYearsAgo", "residenceType", "yearsAtCurrentAddress",
    "loanTerm", "interestRate", "collateralType", "collateralValue", "guarantorStatus",
    "guarantorCreditScore", "guarantorIncome", "guarantorRelationship"
]

loan_purposes = ["Mortgage", "Car", "Personal", "Business", "Education"]
marital_statuses = ["Single", "Married", "Divorced"]
education_levels = ["High School", "Bachelors", "Masters", "PhD"]
industries = ["Technology", "Healthcare", "Retail", "Finance", "Education"]
risk_categories = ["Low", "Medium", "High"]
statuses = ["Approved", "Rejected"]
employment_types = ["Full-time", "Part-time", "Self-employed"]
residence_types = ["Own", "Rent", "Mortgage"]
loan_terms = ["Short-term", "Medium-term", "Long-term"]
collateral_types = ["Real Estate", "Vehicle", "None"]
relationships = ["Parent", "Spouse", "Sibling", "None"]

def random_birth_date_and_age():
    birth_date = fake.date_of_birth(minimum_age=21, maximum_age=65)
    age = datetime.now().year - birth_date.year
    return birth_date.strftime("%Y-%m-%d"), age

def generate_customer_row(i):
    birth_date, age = random_birth_date_and_age()
    has_bankruptcy = random.choice([True, False])
    has_foreclosure = random.choice([True, False])
    
    return [
        f"CUST{i:03d}",  
        fake.name(), 
        random.randint(500, 800), 
        random.randint(30000, 150000),  
        round(random.uniform(0.2, 0.7), 2),  
        random.randint(70, 100), 
        random.randint(1, 15), 
        random.randint(50000, 250000),  
        random.choice(loan_purposes),
        random.randint(0, 4), 
        random.randint(150000, 600000),
        random.choice(marital_statuses),
        random.choice(education_levels),
        random.choice(industries),
        random.choice(risk_categories),
        random.choice(statuses),
        fake.sentence(nb_words=3),  
        fake.sentence(nb_words=4), 
        birth_date,
        age,
        random.choice(employment_types),
        random.randint(2000, 6000), 
        random.randint(5000, 200000), 
        random.randint(1, 25),  
        random.randint(1, 5), 
        round(random.uniform(0.1, 0.9), 2),  
        has_bankruptcy,
        random.randint(1, 10) if has_bankruptcy else 0,
        has_foreclosure,
        random.randint(1, 10) if has_foreclosure else 0,
        random.choice(residence_types),
        random.randint(1, 15),  
        random.choice(loan_terms),
        round(random.uniform(3.5, 8.0), 2),  
        random.choice(collateral_types),
        random.randint(0, 600000),
        random.choice(["None", "Required"]),
        random.randint(600, 750) if random.random() < 0.5 else "",
        random.randint(30000, 100000) if random.random() < 0.5 else "",
        random.choice(relationships) if random.random() < 0.5 else "",
    ]

def generate_csv(filename, n):
    with open(filename, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(headers)
        for i in range(1, n + 1):
            writer.writerow(generate_customer_row(i))

generate_csv("synthetic_loan_data.csv", 100)

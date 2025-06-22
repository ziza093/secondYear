from functional import seq
from datetime import date, datetime
from dataclasses import dataclass

@dataclass
class Person:
    first_name: str
    last_name: str
    date_of_birth: date
    email_address: str
    
    def __str__(self):
        return f"{self.first_name} {self.last_name}, {self.date_of_birth.strftime('%d %b %Y')}"

def calculate_age(birth_date):
    today = date.today()
    return today.year - birth_date.year - ((today.month, today.day) < (birth_date.month, birth_date.day))

def main():
    persons = [
        Person("John", "Doe", date(1960, 11, 3), "jdoe@example.com"),
        Person("Ellen", "Smith", date(1992, 5, 13), "ellensmith@example.com"),
        Person("Jane", "White", date(1986, 2, 1), "janewhite@example.com"),
        Person("Bill", "Jackson", date(1999, 11, 6), "bjackson@example.com"),
        Person("John", "Smith", date(1975, 7, 14), "johnsmith@example.com"),
        Person("Jack", "Williams", date(2005, 5, 28), "")
    ]
    
    # Convert to PyFunctional sequence
    persons_seq = seq(persons)
    
    # Sort by age and find youngest and oldest person
    youngest = persons_seq.sorted(key=lambda person: person.date_of_birth, reverse=True).first()
    oldest = persons_seq.sorted(key=lambda person: person.date_of_birth).first()
    print(f"youngest person is: {youngest}")
    print(f"oldest person is: {oldest}\n")
    
    # Filter under age
    underage = persons_seq.filter(lambda person: calculate_age(person.date_of_birth) < 18).to_list()
    print(f"underage: {underage}\n")
    
    # List of emails
    emails = persons_seq.map(lambda person: person.email_address).to_list()
    print(f"emails: {emails}\n")
    
    # Map of name and email
    emails_map = persons_seq.map(lambda person: (f"{person.first_name} {person.last_name}", person.email_address)).to_dict()
    print(f"emails map: {emails_map}\n")
    
    # Map of email and person
    email_person_map = persons_seq.map(lambda person: (person.email_address, person)).to_dict()
    for email, person in email_person_map.items():
        print(f"{email}: {person}")
    print()
    
    # Group by month of birthday
    people_to_celebrate_each_month = persons_seq.group_by(lambda person: person.date_of_birth.month)
    print(f"birthdays each month: {people_to_celebrate_each_month}\n")
    
    # Partition
    # In PyFunctional, partition returns a tuple of sequences, not two separate values
    partition_result = persons_seq.partition(lambda person: person.date_of_birth.year <= 1980)
    born_before_1980 = partition_result[0]
    born_after_1980 = partition_result[1]
    print(f"born before 1980: {born_before_1980.to_list()}")
    print(f"born after 1980: {born_after_1980.to_list()}\n")
    
    # Distinct first names
    names = persons_seq.map(lambda person: person.first_name).distinct().to_list()
    print(f"first names: {', '.join(names)}\n")
    
    # Average age
    average_age = persons_seq.map(lambda person: calculate_age(person.date_of_birth)).average()
    print(f"Average age: {average_age}\n")
    
    # Count
    # In PyFunctional, we use size() to count elements after filtering
    smiths = persons_seq.filter(lambda person: person.last_name == "Smith").size()
    print(f"number of people called Smith: {smiths}\n")
    
    # Find any with optional result using head_option()
    optional = persons_seq.filter(lambda person: person.first_name == "John").head_option()
    if optional:
        print(optional)
    else:
        print("No one named John was found")
    print()
    
    # Find any with optional and alternative result
    search_result = persons_seq.filter(lambda person: person.first_name == "Thomas").map(lambda person: person.last_name).head_option()
    if search_result:
        print(search_result)
    else:
        print("No one named Thomas was found.")
    print()
    
    # Check any with missing email
    no_email = persons_seq.exists(lambda person: person.email_address == "")
    print(f"any with missing email: {no_email}")

if __name__ == "__main__":
    main()
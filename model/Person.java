package cs240.evanjones.server.model;

/** A unique person with name (First and Last) and Family */
public class Person
{
    /**Persons unique ID (non-empty)*/
    private String personID;
    /**User whom the person belongs*/
    private String descendant;
    /**First Name of Person (non-empty)*/
    private String firstName;
    /**Last Name of Person (non-empty)*/
    private String lastName;
    /**Gender of Person (non-empty)*/
    private String gender;
    /**Optional Father of Person*/
    private String father;
    /**Optional Mother of Person*/
    private String mother;
    /**Optional Spouse of Person*/
    private String spouse;

    /**Creates Person and sets fields*/
    public Person(String personId, String descendant, String firstName,
                  String lastName, String gender) {
        this.personID = personId;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getPersonId()
    {
        return personID;
    }

    public void setPersonId(String personID)
    {
        this.personID = personID;
    }

    public String getDescendant()
    {
        return descendant;
    }

    public void setDescendant(String descendant)
    {
        this.descendant = descendant;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getFather()
    {
        return father;
    }

    public void setFather(String father)
    {
        this.father = father;
    }

    public String getMother()
    {
        return mother;
    }

    public void setMother(String mother)
    {
        this.mother = mother;
    }

    public String getSpouse()
    {
        return spouse;
    }

    public void setSpouse(String spouse)
    {
        this.spouse = spouse;
    }
}

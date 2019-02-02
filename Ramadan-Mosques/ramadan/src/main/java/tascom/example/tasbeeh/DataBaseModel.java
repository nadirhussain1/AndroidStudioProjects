package tascom.example.tasbeeh;


public class DataBaseModel
{

    private int count;
    private String description;
    private int key_id;
    private String title;

    public DataBaseModel()
    {
    }

    public int getCount()
    {
        return count;
    }

    public String getDescription()
    {
        return description;
    }

    public int getKey_id()
    {
        return key_id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setCount(int i)
    {
        count = i;
    }

    public void setDescription(String s)
    {
        description = s;
    }

    public void setKey_id(int i)
    {
        key_id = i;
    }

    public void setTitle(String s)
    {
        title = s;
    }
}

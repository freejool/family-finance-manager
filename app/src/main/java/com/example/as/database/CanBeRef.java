package com.example.as.database;

import java.sql.ResultSet;

public class CanBeRef<T>
{
    public T value;

    public  CanBeRef(IFromResultset<T> fromdb,IToDatabaseValue<T,?> todb)
    {
        how_to_get_from_result_set=fromdb;
        how_to_convert_to_db_data_version=todb;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public void setValueByResultset(ResultSet rs,String col_name)
    {
        this.value = how_to_get_from_result_set.run(rs,col_name);
    }

    public IFromResultset<T> how_to_get_from_result_set;
    public IToDatabaseValue<T,?> how_to_convert_to_db_data_version;
}

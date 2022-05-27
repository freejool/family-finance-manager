package com.example.as.database;

import java.sql.ResultSet;

/**
 * Java中有很多类型的对象通过修改指向对象的方法来修改，而不是修改对象本身，因此一般的类型绑定后可能会丢失引用。
 * 此类型包含一个{@link CanBeRef#value}值用来承载这种类型的实例，使得绑定效果能够持续。
 * <p>此外为了方便使用，此类型实例也会记录{@link IFromResultset}接口实例来自动化从结果集抓取数据的流程，
 * 也会记录{@link IToDatabaseValue}接口实例来自动化从成员转换为数据库所需字符串的流程。</p>
 * @param <T>
 */
public class CanBeRef<T>
{
    public T value;
    public static final String null_hint="null";

    public  CanBeRef(IFromResultset<T> fromdb,IToDatabaseValue<T,String> todb)
    {
        how_to_get_from_result_set=fromdb;
        how_to_convert_to_db_data_version=todb;
    }

    @Override
    public String toString() {
        if(value==null)
            return null_hint;
        else
            return value.toString();
    }

    public void setValueByResultset(ResultSet rs,String col_name)
    {
        this.value = how_to_get_from_result_set.run(rs,col_name);
    }

    public String getSqlValues()
    {
        return how_to_convert_to_db_data_version.run(value);
    }

    public IFromResultset<T> how_to_get_from_result_set;
    public IToDatabaseValue<T,String> how_to_convert_to_db_data_version;
}

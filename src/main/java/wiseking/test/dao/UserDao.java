package wiseking.test.dao;

import java.util.List;
import wiseking.test.entity.UserEntity;

/**
 * 数据访问对象 DAO 作为用户数据的访问接口
 *
 */
public interface UserDao {

    String insertUser(UserEntity user);

    String updateUser(UserEntity user);

    List<UserEntity> queryInfo(String type, Object value);

}

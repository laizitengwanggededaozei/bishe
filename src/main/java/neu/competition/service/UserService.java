package neu.competition.service;

import com.github.pagehelper.PageInfo;
import neu.competition.entity.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public abstract interface UserService {
    // 新增方法
    boolean existsById(String id);
    boolean existsByUname(String uname);
    boolean existsByUmail(String umail);


    User findByIdAndPassword(String id, String upwd);

    public abstract boolean register(User user);


    PageInfo<User> getUseridList(Integer pageNum, Integer pageSize);

    List<User> getUserList(User user);

    public abstract User login(String uname, String upwd);

    /**
     * 查询所有用户
     */
    public abstract long findAll();

    /**
     * 查询单个
     */
    public abstract User findById(String id);

    /**
     * 修改信息
     */
    boolean updateUser(User user);

    /**
     * 删除用户
     */
    public abstract boolean deleteUser(String id);

    boolean register(String umail, String uname, String upwad);


    void Save(User user);

    @Transactional
    void updateUserProfile(User loggedUser, User updatedUser, MultipartFile profilePicFile) throws Exception;
}






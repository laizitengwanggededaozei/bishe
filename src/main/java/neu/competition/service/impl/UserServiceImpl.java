package neu.competition.service.impl;

//import mapper.User;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import neu.competition.entity.User;
import neu.competition.mapper.UserMapper;
import neu.competition.service.FileService2;
import neu.competition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
//import service.UserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileService2 fileService2;
    @Override
    public boolean existsById(String id) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("id", id)) > 0;
    }

    @Override
    public boolean existsByUname(String uname) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("uname", uname)) > 0;
    }

    @Override
    public boolean existsByUmail(String umail) {
        return userMapper.selectCount(new QueryWrapper<User>().eq("umail", umail)) > 0;
    }


    @Override
    public void Save(User user) {
        userMapper.insert(user);
    }

    @Override
    public User findByIdAndPassword(String id, String upwd) {
        return userMapper.findByIdAndPassword(id, upwd);
    }

    @Override
    public boolean register(User user) {
        // 插入用户信息到数据库
        int result = userMapper.insert(user);
        return result > 0;
    }

    @Override
    public User findById(String id) {
        // 根据用户ID查询用户信息
        return userMapper.getUserById(id);
    }


    @Override
    public boolean updateUser(User user) {
        // 根据用户ID更新用户信息
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    public boolean deleteUser(String id) {
        // 根据用户ID删除用户信息
        int result = userMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public boolean register(String umail, String uname, String upwd) {
        User user = new User();
        user.setUmail(umail);
        user.setUname(uname);
        user.setUpwd(upwd);
        return register(user);
    }


    @Override
    public PageInfo<User> getUseridList(Integer pageNum, Integer pageSize) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        // 查询所有用户列表
        List<User> userList = userMapper.selectList(null);
        // 返回分页信息对象
        return new PageInfo<>(userList);
    }

    @Override
    public List<User> getUserList(User user) {
        // 根据用户条件查询用户列表
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public User login(String uname, String upwd) {
        // 根据用户名和密码查询用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uname", uname).eq("upwd", upwd);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public long findAll() {
        // 查询所有用户数量
        return userMapper.selectCount(null);
    }

    @Override
    @Transactional
    public void updateUserProfile(User loggedUser, User updatedUser, MultipartFile profilePicFile) throws Exception {
        // 更新基本信息
        loggedUser.setUmail(updatedUser.getUmail());

        // 如果密码为空，保持原密码
        if (updatedUser.getUpwd() != null && !updatedUser.getUpwd().isEmpty()) {
            loggedUser.setUpwd(updatedUser.getUpwd());
        }

        // 处理文件上传
        if (profilePicFile != null && !profilePicFile.isEmpty()) {
            String profilePicUrl = fileService2.saveFile(profilePicFile);
            if (profilePicUrl != null) {
                loggedUser.setUprofilepic(profilePicUrl);
            } else {
                throw new Exception("文件上传失败");
            }
        }

        // 更新用户信息
        userMapper.updateById(loggedUser);
    }


    /**
     * @see service.UserService#register(mapper.User)
     */
//    public boolean register(User user) {return false;}

}
package neu.competition.define;

import neu.competition.base.ServiceException;

/**
 * 店铺相关业务处理产生的异常预定义
 *
 * @author lemonit.cn-liuri
 */
public class ShopServiceExceptionDefine {
    public static final ServiceException APPLY_SHOP_NOT_EXISTS = new ServiceException("您访问的店铺不存在", "APPLY_SHOP_NOT_EXISTS", 404, "");
}

package neu.competition.define;

import neu.competition.base.ServiceException;

/**
 * 订单相关业务处理产生的异常预定义
 *
 * @author lemonit.cn-liuri
 */
public class OrderServiceExceptionDefine {
    public static final ServiceException NOT_SELECT_GOODS = new ServiceException("您还未选择要结算的商品", "NOT_SELECT_GOODS", 404, "");
}

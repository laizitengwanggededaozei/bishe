package neu.competition.service.factory;

import neu.competition.entity.CompetitionType;
import neu.competition.mapper.CompetitionMapper;
import neu.competition.mapper.CompetitionTypeMapper;
import neu.competition.service.strategy.CompetitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CompetitionStrategyFactory {
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private CompetitionMapper competitionMapper;
    
    @Autowired
    private CompetitionTypeMapper competitionTypeMapper;
    
    public CompetitionStrategy getStrategy(Integer matchId) {
        // 根据比赛ID获取赛事ID
        Integer comId = competitionMapper.selectById(matchId).getComId();
        
        // 根据赛事ID获取学科
        String discipline = competitionMapper.selectById(comId).getDiscipline();
        
        // 根据学科获取比赛类型
        CompetitionType type = competitionTypeMapper.selectByDiscipline(discipline);
        
        if (type == null) {
            throw new RuntimeException("未找到对应的比赛类型");
        }
        
        // 根据类型名称获取对应的策略Bean
        String strategyBeanName = type.getTypeName().toLowerCase() + "Strategy";
        
        if (!context.containsBean(strategyBeanName)) {
            throw new RuntimeException("未找到对应的比赛策略Bean: " + strategyBeanName);
        }
        
        return (CompetitionStrategy) context.getBean(strategyBeanName);
    }
}
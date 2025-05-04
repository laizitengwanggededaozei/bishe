package neu.competition.service.factory;

import neu.competition.entity.Competition;
import neu.competition.entity.CompetitionType;
import neu.competition.entity.Matches;
import neu.competition.mapper.CompetitionMapper;
import neu.competition.mapper.CompetitionTypeMapper;
import neu.competition.mapper.MatchMapper;
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

    // 添加MatchMapper注入
    @Autowired
    private MatchMapper matchMapper;

    @Autowired
    private CompetitionTypeMapper competitionTypeMapper;

    public CompetitionStrategy getStrategy(Integer matchId) {
        try {
            // 使用matchMapper而不是competitionMapper来查询Matches对象
            Matches match = matchMapper.selectById(matchId);
            if (match == null) {
                System.out.println("未找到比赛ID: " + matchId);
                return (CompetitionStrategy) context.getBean("programmingStrategy");
            }

            Integer comId = match.getComId();

            // 使用competitionMapper查询Competition对象
            Competition competition = competitionMapper.selectById(comId);
            if (competition == null) {
                System.out.println("未找到赛事ID: " + comId);
                return (CompetitionStrategy) context.getBean("programmingStrategy");
            }

            String discipline = competition.getDiscipline();
            System.out.println("找到学科: " + discipline);

            // 根据学科获取比赛类型
            CompetitionType type = competitionTypeMapper.selectByDiscipline(discipline);
            if (type == null) {
                System.out.println("未找到学科对应的比赛类型: " + discipline);
                // 使用默认策略
                return (CompetitionStrategy) context.getBean("programmingStrategy");
            }

            // 根据类型名称获取对应的策略Bean
            String strategyBeanName = type.getTypeName().toLowerCase() + "Strategy";
            System.out.println("使用策略: " + strategyBeanName);

            if (!context.containsBean(strategyBeanName)) {
                System.out.println("未找到对应的比赛策略Bean: " + strategyBeanName);
                return (CompetitionStrategy) context.getBean("programmingStrategy");
            }

            return (CompetitionStrategy) context.getBean(strategyBeanName);
        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时使用默认策略
            return (CompetitionStrategy) context.getBean("programmingStrategy");
        }
    }
}

package neu.competition.controller;

import neu.competition.DTO.CompetitionDTO;
import neu.competition.DTO.MatchesDTO;
import neu.competition.mapper.MatchMapper;
import neu.competition.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 商城视图
 *
 * @author lemonit.cn-liuri
 */
@Controller
public class HomeController {
    @Autowired
    private HttpSession session;
    @Autowired
    private CompetitionService competitionService;

    /**
     * 商城首页视图
     *
     * @return 商城首页视图
     */
    @GetMapping("/")
    public String mallIndex(Model model) {
/*        List<Shop> newShopList = shopService.searchShop(0, "").getList().subList(0, 5);
        model.addAttribute("shopList", newShopList);
        List<GoodsDTO> newGoodsList = goodsService.listLatestGoods(10);
        model.addAttribute("goodsList", newGoodsList);*/
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String goIndex(Model model) {
        List<CompetitionDTO> comList = competitionService.selectCommend();
        List<MatchesDTO> matchList = competitionService.selectNewMatches();
        model.addAttribute("competitionDTOList", comList);
        model.addAttribute("matchesDTOList", matchList);
        return "competition/index";
    }

    @GetMapping("/adminhome")
    public String adminHome(){
        return "competition/adminhome";
    }

    @GetMapping("/admin/create-competition")
    public String createCompetition(){
        return "competition/create-competition";
    }

    @GetMapping("/admin/create-match")
    public String createMatch(){
        return "competition/create-match";
    }

    @GetMapping("/admin/create-team")
    public String createTeam(){
        return "team/create-team";
    }


}

package liuru.world.vote_master.service.impl;

import liuru.world.vote_master.entity.Rank;
import liuru.world.vote_master.mapper.RankMapper;
import liuru.world.vote_master.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankServiceImpl implements RankService {
    @Autowired
    private RankMapper rankMapper;
    @Override
    public Integer insertRank(Rank rank) {
        return rankMapper.insertRank(rank);
    }
}

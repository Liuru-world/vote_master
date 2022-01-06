package liuru.world.vote_master.mapper;

import liuru.world.vote_master.entity.Rank;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface RankMapper {
    public Integer insertRank(Rank rank);

}

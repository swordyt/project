package entity.inter;

import java.util.List;

import entity.entity.t_ajd_agent;

public interface t_ajd_agent_inter {
	public List<t_ajd_agent> selectBysMobile(String sMobile);
}

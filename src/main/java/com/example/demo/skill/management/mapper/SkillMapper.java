package com.example.demo.skill.management.mapper;

import com.example.demo.skill.management.dto.SkillsListingDTO;
import com.example.demo.skill.management.model.SkillsListing;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SkillMapper {
    private final ModelMapper modelMapper;

    public Page<SkillsListingDTO> allSkillsListingToSkillsListingDto(Pageable pageable,Page<SkillsListing> skillsListingList){
//        return skillsListingList.map(skills->modelMapper.map(skills, SkillsListingDTO.class));
     return new PageImpl<>
                (skillsListingList.map(skills->modelMapper.map(skills, SkillsListingDTO.class)).toList(),pageable,skillsListingList.getTotalElements());
    }
    public SkillsListingDTO skillListingDtoById(SkillsListing skillsListing){
        return  modelMapper.map(skillsListing, SkillsListingDTO.class);
    }
}

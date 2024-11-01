package com.example.mindshare.db.member

import com.example.mindshare.db.member.QMemberEntity.memberEntity
import com.example.mindshare.domain.member.MemberRepository
import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto
import com.example.mindshare.security.CustomPasswordEncoder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
internal class MemberEntityRepository(
    private val memberJpaRepository: MemberJpaRepository,
    private val query: JPAQueryFactory,
    private val custompasswordEncoder: CustomPasswordEncoder,
) : MemberRepository {

    override fun createMember(createMemberDto: CreateMemberDto): MemberInfoDto {
        val savedMember = memberJpaRepository.save(MemberEntity.of(createMemberDto, encodePassword(createMemberDto.password)))
        return savedMember.toMemberInfoDto()
    }

    override fun modifyMember(modifyMemberDto: ModifyMemberDto): MemberInfoDto {
        val memberByLoginId = query
            .selectFrom(memberEntity)
            .where(memberEntity.uniqueId.eq(modifyMemberDto.memberUniqueId))
            .fetchOne() ?: throw NoSuchElementException("Member not found")

        // 명시성을 위한 save
        val updatedMember = memberJpaRepository.save(memberByLoginId.update(modifyMemberDto, encodePassword(modifyMemberDto.password)))
        return updatedMember.toMemberInfoDto()
    }

    override fun checkExistedMember(loginId: String): com.example.mindshare.domain.member.Member? {
        val member = query
            .selectFrom(memberEntity)
            .where(memberEntity.loginId.eq(loginId))
            .fetchOne()
            ?.let { MemberEntity.toDomain(it) }

        return member
    }

    private fun encodePassword(password: String?): String? {
        return password?.let { custompasswordEncoder.encode(it) }
    }
}

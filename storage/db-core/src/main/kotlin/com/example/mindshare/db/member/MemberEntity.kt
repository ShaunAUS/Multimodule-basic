package com.example.mindshare.db.member

import com.example.mindshare.db.BaseEntity
import com.example.mindshare.domain.member.Member
import com.example.mindshare.domain.member.dto.CreateMemberDto
import com.example.mindshare.domain.member.dto.MemberInfoDto
import com.example.mindshare.domain.member.dto.ModifyMemberDto
import core.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "member")
class MemberEntity(
    @Column
    var password: String,
    @Column
    var name: String,
    @Column
    var age: Int,
    @Column(unique = true)
    var loginId: String,
    @Column(nullable = false, unique = true, updatable = false)
    var uniqueId: String = UUID.randomUUID().toString(),
    @Column
    var role: Int,

) : BaseEntity() {

    companion object {
        fun of(createMemberDto: CreateMemberDto, encodedPassword: String?): MemberEntity {
            return MemberEntity(
                password = encodedPassword!!,
                name = createMemberDto.name!!,
                age = createMemberDto.age!!,
                loginId = createMemberDto.loginId!!,
                role = Role.fromRoleName(createMemberDto.role!!),
            )
        }

        fun toDomain(memberEntity: MemberEntity): Member {
            return Member(
                id = memberEntity.id!!,
                password = memberEntity.password,
                name = memberEntity.name,
                loginId = memberEntity.loginId,
                role = "STUDENT",
            )
        }
    }

    fun toMemberInfoDto(): MemberInfoDto {
        return MemberInfoDto(this.name, this.age, Role.fromNumber(this.role).roleName)
    }

    fun update(modifyMemberDto: ModifyMemberDto, encodedPassword: String?): MemberEntity {
        password = encodedPassword ?: password
        name = modifyMemberDto.name ?: name
        age = modifyMemberDto.age ?: age
        loginId = modifyMemberDto.loginId ?: loginId
        role = Role.fromRoleName(modifyMemberDto.role!!)
        return this
    }
}

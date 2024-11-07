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
    password: String,
    name: String,
    age: Int,
    loginId: String,
    role: Int,
) : BaseEntity() {

    @Column
    var password: String = password
        protected set

    @Column
    var name: String = name
        protected set

    @Column
    var age: Int = age
        protected set

    @Column(unique = true)
    var loginId: String = loginId
        protected set

    @Column
    var role: Int = role
        protected set

    @Column(nullable = false, unique = true, updatable = false)
    var uniqueId: String = UUID.randomUUID().toString()


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

package com.example.sztoswro.member

import com.example.sztoswro.emails.EmailSender.Companion.sendValidationEmail
import com.example.sztoswro.exceptions.BadRequestException
import com.example.sztoswro.exceptions.NoContentException
import com.example.sztoswro.exceptions.UserAlreadyExists
import com.example.sztoswro.member.Validator.Companion.validate
import com.example.sztoswro.member.Validator.Companion.validateRegistrationData
import com.example.sztoswro.organization.OrganizationDAO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository,
                    private val roleService: RoleService){
    private val logger: Logger = LoggerFactory.getLogger(MemberService::class.simpleName)

    fun getAll(): Iterable<MemberDAO> { return memberRepository.findAll()}

    fun getMember(id: Long): MemberDAO {
        val member = memberRepository.findById(id)
        if (member.isPresent)
            return member.get()
        else
            throw NoContentException("Member with id = {id} not found.")
    }

    fun getByEmail(username: String): MemberDAO {
        return memberRepository.getMemberDAOByEmail(username).orElseThrow { throw NoContentException("Member with id = {id} not found.") }
    }

    fun findMember(email: String): MemberDAO {
        val member = memberRepository.findByEmail(email)
        if (member.isPresent)
            return member.get()
        else
            throw NoContentException("Member with id = {id} not found.")
    }

    fun addMember(memberDAO: MemberDAO) {
        val aUser = memberRepository.findByEmail(memberDAO.email)

        if ( !aUser.isPresent ) {
            memberDAO.password = BCryptPasswordEncoder().encode(memberDAO.password)
            memberRepository.save(memberDAO)
        } else {
            throw UserAlreadyExists(memberDAO.email)
        }
    }

    fun deleteMember(memberDAO: MemberDAO) {
    }

    fun deleteAll() {
        memberRepository.deleteAll()
    }

    fun registerMember(memberDAO: MemberDAO) {
        val errors: List<Error> = validateRegistrationData(memberDAO)

        if (errors.isEmpty()) {
            memberDAO.isEmailValidated = false
            addMember(memberDAO)
            sendValidationEmail()
        } else {
            for (error in errors) {
                logger.error(error.message)
            }
            val errorMessage: String = errors[0].message.orEmpty()
            throw BadRequestException(errorMessage)
        }
    }

    fun editData(id: Long, memberDAO: MemberDAO) {
        val errors: List<Error> = validate(memberDAO)

        if(errors.isEmpty()) {
            val member: MemberDAO = memberRepository.findById(id).orElseThrow { throw NoContentException("Member not found") }
            memberDAO.faculty.let { member.faculty = it }
            memberDAO.phoneNumber.let{ member.phoneNumber = it }
            memberDAO.studYear.let { member.studYear = it }
            memberDAO.department.let { member.department = it }
            memberDAO.university.let { member.university = it }
            memberDAO.birthDate.let { member.birthDate = it }
            memberDAO.status.let { member.status = it }
            memberDAO.isActive.let { member.isActive = it }
            memberDAO.ICENumber.let { member.ICENumber = it }

            memberRepository.save(member)
        } else {
            for (error in errors) {
                logger.error(error.message)
            }
            val errorMessage: String = errors[0].message.orEmpty()
            throw BadRequestException(errorMessage)
        }
    }

    fun addRole(organizationId: Long, projectId: Long, roleLevel: RoleLevel, memberId: Long) {
        val member = getMember(memberId)
        if (member.roles == null) {
            member.roles = Roles()
        }
            roleService.addRole(member.roles!!, organizationId, projectId, roleLevel)
        memberRepository.save(member)
    }

    fun getRole(organizationId: Long, projectId: Long, memberId: Long): String {
        getMember(memberId).roles?.roleMap?.get(organizationId)?.projectRoleMap?.get(projectId)?.let {
            roleLevel -> return roleLevel.name
        }
        return RoleLevel.none.name
    }

    fun addOrganization(memberDao: MemberDAO, organizationDAO: OrganizationDAO) {
        addRole(organizationDAO.id, 0, RoleLevel.basic, memberDao.id)
        memberRepository.save(memberDao)
    }
}


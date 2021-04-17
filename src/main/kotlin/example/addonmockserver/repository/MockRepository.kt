package example.addonmockserver.repository

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet

interface MockRepository {
    fun all(): List<MockInfo>
    fun findByPath(path: String): MockInfo?
    fun setMockResponse(mockInfo: MockInfo)
    fun deleteAll()
    fun deleteByPath(path: String)
}

@Repository
class MockRepositoryImpl(val jdbcTemplate: JdbcTemplate): MockRepository {
    override fun all(): List<MockInfo> {
        return jdbcTemplate.query(
                "select * from mock_response order by path", mockInfoRowMapper
        )
    }

    override fun findByPath(path: String): MockInfo? {
        return try {
            jdbcTemplate.queryForObject(
                    """
                    select * from mock_response 
                    where path = ? limit 1 
                    """,
                    mockInfoRowMapper, path
            )
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun setMockResponse(mockInfo: MockInfo) {
        jdbcTemplate.update {
            val statement = it.prepareStatement(
                    """
                insert into mock_response (path, expected_status_code, expected_body)
                values (?, ?, ?)
                on duplicate key update expected_status_code = ?, expected_body = ?
            """
            )
            statement.setString(1, mockInfo.path)
            statement.setInt(2, mockInfo.expectedStatusCode)
            statement.setString(3, mockInfo.expectedBody)
            statement.setInt(4, mockInfo.expectedStatusCode)
            statement.setString(5, mockInfo.expectedBody)
            statement
        }
    }

    override fun deleteAll() {
        jdbcTemplate.update {
            it.prepareStatement("truncate mock_response")
        }
    }

    override fun deleteByPath(path: String) {
        jdbcTemplate.update {
            val statement = it.prepareStatement(
            "delete from mock_response where path = ?"
            )
            statement.setString(1, path)
            statement
        }
    }

    private val mockInfoRowMapper = { rs: ResultSet, _: Int ->
        MockInfo(
                rs.getString("path"),
                rs.getInt("expected_status_code"),
                rs.getString("expected_body")
        )
    }
}

data class MockInfo(
        val path: String,
        val expectedStatusCode: Int,
        val expectedBody: String
)

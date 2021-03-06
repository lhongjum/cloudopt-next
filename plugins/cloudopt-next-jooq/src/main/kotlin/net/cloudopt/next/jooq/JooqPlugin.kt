/*
 * Copyright 2017-2021 Cloudopt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.cloudopt.next.jooq

import net.cloudopt.next.jooq.pool.ConnectionPool
import net.cloudopt.next.jooq.pool.HikariCPPool
import net.cloudopt.next.utils.Classer
import net.cloudopt.next.web.Plugin
import net.cloudopt.next.web.config.ConfigManager
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultTransactionProvider
import java.sql.SQLException
import kotlin.reflect.full.createInstance


/*
 * @author: Cloudopt
 * @Time: 2018/2/6
 * @Description: Jooq plugin of cloudopt next
 */
class JooqPlugin : Plugin {

    override fun start(): Boolean {

        System.getProperties().setProperty("org.jooq.no-logo", "true")

        try {
            var map = ConfigManager.init("jooq")

            var pool: ConnectionPool = HikariCPPool()

            if (map.get("pool") != null) {
                pool = Classer.loadClass(map.get("pool") as String).createInstance() as ConnectionPool
            }

            var sqlDialect = when (map.get("database")) {
                "mysql" -> SQLDialect.MYSQL
                "derby" -> SQLDialect.DERBY
                "firebird" -> SQLDialect.FIREBIRD
                "mariadb" -> SQLDialect.MARIADB
                "postgres" -> SQLDialect.POSTGRES
                "sqlite" -> SQLDialect.SQLITE
                else -> {
                    SQLDialect.MYSQL
                }
            }
            JooqManager.connection = pool.getConnection()
            JooqManager.connectionProvider = DataSourceConnectionProvider(pool.getDatasource())
            JooqManager.transactionProvider = DefaultTransactionProvider(JooqManager.connectionProvider)
            JooqManager.configuration.set(JooqManager.connectionProvider)
                .set(JooqManager.transactionProvider)
                .set(sqlDialect)
                .set(JooqManager.settings)
            JooqManager.dsl = DSL.using(JooqManager.configuration)
            return true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }

    }

    override fun stop(): Boolean {
        try {
            JooqManager.connection?.close()
            return true
        } catch (e: SQLException) {
            e.printStackTrace()
            return false
        }

    }
}
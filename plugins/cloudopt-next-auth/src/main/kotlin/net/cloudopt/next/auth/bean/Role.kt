/*
 * Copyright 2017-2020 original authors
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
package net.cloudopt.next.auth.bean

import java.util.*

/**
 * Used to record verification roles.
 * @property id role id
 * @property name role name
 * @property rules LinkedList<Rule>
 * @see Rule
 * @constructor
 */
data class Role(
    var id: Int = 0,
    var name: String = "",
    var rules: LinkedList<Rule> = LinkedList()
)
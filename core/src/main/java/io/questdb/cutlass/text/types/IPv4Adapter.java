/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 *
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2023 QuestDB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.cutlass.text.types;

import io.questdb.cairo.ColumnType;
import io.questdb.cairo.TableWriter;
import io.questdb.griffin.SqlKeywords;
import io.questdb.std.Numbers;
import io.questdb.std.NumericException;
import io.questdb.std.str.DirectByteCharSequence;

public final class IPv4Adapter extends AbstractTypeAdapter {

    public static final IPv4Adapter INSTANCE = new IPv4Adapter();

    private IPv4Adapter() {
    }

    @Override
    public int getType() {
        return ColumnType.IPv4;
    }

    @Override
    public boolean probe(DirectByteCharSequence text) {
        if (text.length() < 7)
            return false;
        if (Numbers.notDigit(text.charAt(0))) {
            if (text.charAt(0) != '.') {
                return false;
            }
        }

        try {
            Numbers.parseIPv4(text);
            return true;
        } catch (NumericException e) {
            return false;
        }
    }

    @Override
    public void write(TableWriter.Row row, int column, DirectByteCharSequence value) throws Exception {
        row.putInt(column, SqlKeywords.isNullKeyword(value) ? Numbers.IPv4_NULL : parseIPv4(value));
    }

    private int parseIPv4(DirectByteCharSequence value) throws NumericException {
        return Numbers.parseIPv4(value);
    }


}

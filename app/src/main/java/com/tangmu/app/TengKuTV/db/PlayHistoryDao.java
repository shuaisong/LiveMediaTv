/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tangmu.app.TengKuTV.db;

import android.content.Context;

public class PlayHistoryDao {
    public static final String TABLE_NAME = "PlayHistoryInfo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_B_ID = "b_id";
    public static final String COLUMN_B_TITLE = "b_title";
    public static final String COLUMN_B_TITLE_Z = "b_title_z";
    public static final String COLUMN_B_IMG = "b_img";
    public static final String COLUMN_B_PROGRESS = "b_progress";
    public static final String COLUMN_B_POSITION = "b_position";
    public static final String COLUMN_B_UPDATE_TIME = "b_update_time";
    public static final String COLUMN_B_TYPE = "b_type";//1 视频 2音频
    public static final String COLUMN_VM_TYPE = "vm_type";//1 电视 2电影
    public static final String COLUMN_ID_ONE = "id_one";//1级分类

    public PlayHistoryDao(Context context) {
    }
}

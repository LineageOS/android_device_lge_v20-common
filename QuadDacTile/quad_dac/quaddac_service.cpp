/*
 * Copyright (C) 2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <android/binder_manager.h>
#include <android-base/logging.h>
#include <android-base/properties.h>

#include <sys/mount.h>

#include "quaddac_service.h"

#define AUDIO_PLATFORM_INFO_PATH "/vendor/etc/audio_platform_info.xml"
#define AUDIO_PLATFORM_INFO_QDAC_FILE "/vendor/etc/audio_platform_info-quad_dac.xml"
#define MIXER_PATHS_TASHA_PATH "/vendor/etc/mixer_paths_tasha.xml"
#define MIXED_PATHS_TASHA_QDAC_FILE "/vendor/etc/mixer_paths_tasha-quad_dac.xml"

namespace aidl {
namespace org {
namespace lineageos {
namespace quaddac {

using ::android::base::SetProperty;

void QuadDacService::Register() {
    auto service = ndk::SharedRefBase::make<QuadDacService>();
    binder_status_t status = AServiceManager_addService(
            service->asBinder().get(), getServiceName());

    if (status != STATUS_OK) {
        LOG(FATAL) << "Could not register quaddac service: " << status;
    }
}

ndk::ScopedAStatus QuadDacService::setEnabled(bool enabled) {
    if (enabled) {
        mount(AUDIO_PLATFORM_INFO_QDAC_FILE, AUDIO_PLATFORM_INFO_PATH, NULL, MS_RDONLY | MS_BIND, NULL);
        mount(MIXED_PATHS_TASHA_QDAC_FILE, MIXER_PATHS_TASHA_PATH, NULL, MS_RDONLY | MS_BIND, NULL);
    } else {
        umount(AUDIO_PLATFORM_INFO_PATH);
        umount(MIXER_PATHS_TASHA_PATH);
    }

    android::base::SetProperty("ctl.restart", "audioserver");

    return ndk::ScopedAStatus::ok();
}

}  // namespace quaddac
}  // namespace lineageos
}  // namespace org
}  // namespace aidl

# Audio
PRODUCT_PACKAGES += \
    audio.a2dp.default \
    audio.usb.default \
    audio.r_submix.default \
    audio_policy.default \
    libaudio-resampler \
    libmtk_symbols \
    libtinycompress \
    libtinyxml \
    tinymix

PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio/audio_device.xml:system/etc/audio_device.xml \
    $(LOCAL_PATH)/audio/audio_effects.conf:system/etc/audio_effects.conf \
    $(LOCAL_PATH)/audio/audio_em.xml:system/etc/audio_em.xml \
    $(LOCAL_PATH)/audio/audio_policy.conf:system/etc/audio_policy.conf

	PRODUCT_COPY_FILES += \
    $(LOCAL_PATH)/audio_param/AudioParamOptions.xml:system/etc/audio_param/AudioParamOptions.xml \
    $(LOCAL_PATH)/audio_param/PlaybackACF_AudioParam.xml:system/etc/audio_param/PlaybackACF_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/PlaybackACF_ParamUnitDesc.xml:system/etc/audio_param/PlaybackACF_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/PlaybackDRC_AudioParam.xml:system/etc/audio_param/PlaybackDRC_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/PlaybackDRC_ParamUnitDesc.xml:system/etc/audio_param/PlaybackDRC_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/PlaybackHCF_AudioParam.xml:system/etc/audio_param/PlaybackHCF_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/PlaybackHCF_ParamUnitDesc.xml:system/etc/audio_param/PlaybackHCF_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/PlaybackVolAna_AudioParam.xml:system/etc/audio_param/PlaybackVolAna_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/PlaybackVolAna_ParamUnitDesc.xml:system/etc/audio_param/PlaybackVolAna_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/PlaybackVolDigi_AudioParam.xml:system/etc/audio_param/PlaybackVolDigi_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/PlaybackVolDigi_ParamUnitDesc.xml:system/etc/audio_param/PlaybackVolDigi_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/PlaybackVolUI_AudioParam.xml:system/etc/audio_param/PlaybackVolUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/PlaybackVolUI_ParamUnitDesc.xml:system/etc/audio_param/PlaybackVolUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/Playback_AudioParam.xml:system/etc/audio_param/Playback_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/Playback_ParamTreeView.xml:system/etc/audio_param/Playback_ParamTreeView.xml \
    $(LOCAL_PATH)/audio_param/Playback_ParamUnitDesc.xml:system/etc/audio_param/Playback_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/RecordDMNR_AudioParam.xml:system/etc/audio_param/RecordDMNR_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/RecordDMNR_ParamUnitDesc.xml:system/etc/audio_param/RecordDMNR_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/RecordFIR_AudioParam.xml:system/etc/audio_param/RecordFIR_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/RecordFIR_ParamUnitDesc.xml:system/etc/audio_param/RecordFIR_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/RecordUI_AudioParam.xml:system/etc/audio_param/RecordUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/RecordUI_ParamUnitDesc.xml:system/etc/audio_param/RecordUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/RecordVolUI_AudioParam.xml:system/etc/audio_param/RecordVolUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/RecordVolUI_ParamUnitDesc.xml:system/etc/audio_param/RecordVolUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/RecordVol_AudioParam.xml:system/etc/audio_param/RecordVol_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/RecordVol_ParamUnitDesc.xml:system/etc/audio_param/RecordVol_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/Record_AudioParam.xml:system/etc/audio_param/Record_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/Record_ParamTreeView.xml:system/etc/audio_param/Record_ParamTreeView.xml \
    $(LOCAL_PATH)/audio_param/Record_ParamUnitDesc.xml:system/etc/audio_param/Record_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/SpeechDMNR_AudioParam.xml:system/etc/audio_param/SpeechDMNR_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/SpeechDMNR_ParamUnitDesc.xml:system/etc/audio_param/SpeechDMNR_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/SpeechGeneral_AudioParam.xml:system/etc/audio_param/SpeechGeneral_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/SpeechGeneral_ParamUnitDesc.xml:system/etc/audio_param/SpeechGeneral_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/SpeechMagiClarity_AudioParam.xml:system/etc/audio_param/SpeechMagiClarity_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/SpeechMagiClarity_ParamUnitDesc.xml:system/etc/audio_param/SpeechMagiClarity_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/SpeechUI_AudioParam.xml:system/etc/audio_param/SpeechUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/SpeechUI_ParamUnitDesc.xml:system/etc/audio_param/SpeechUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/SpeechVolUI_AudioParam.xml:system/etc/audio_param/SpeechVolUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/SpeechVolUI_ParamUnitDesc.xml:system/etc/audio_param/SpeechVolUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/SpeechVol_AudioParam.xml:system/etc/audio_param/SpeechVol_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/SpeechVol_ParamUnitDesc.xml:system/etc/audio_param/SpeechVol_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/Speech_AudioParam.xml:system/etc/audio_param/Speech_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/Speech_ParamTreeView.xml:system/etc/audio_param/Speech_ParamTreeView.xml \
    $(LOCAL_PATH)/audio_param/Speech_ParamUnitDesc.xml:system/etc/audio_param/Speech_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VoIPDMNR_AudioParam.xml:system/etc/audio_param/VoIPDMNR_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VoIPDMNR_ParamUnitDesc.xml:system/etc/audio_param/VoIPDMNR_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VoIPGeneral_AudioParam.xml:system/etc/audio_param/VoIPGeneral_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VoIPGeneral_ParamUnitDesc.xml:system/etc/audio_param/VoIPGeneral_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VoIPUI_AudioParam.xml:system/etc/audio_param/VoIPUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VoIPUI_ParamUnitDesc.xml:system/etc/audio_param/VoIPUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VoIPVolUI_AudioParam.xml:system/etc/audio_param/VoIPVolUI_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VoIPVolUI_ParamUnitDesc.xml:system/etc/audio_param/VoIPVolUI_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VoIPVol_AudioParam.xml:system/etc/audio_param/VoIPVol_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VoIPVol_ParamUnitDesc.xml:system/etc/audio_param/VoIPVol_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VoIP_AudioParam.xml:system/etc/audio_param/VoIP_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VoIP_ParamTreeView.xml:system/etc/audio_param/VoIP_ParamTreeView.xml \
    $(LOCAL_PATH)/audio_param/VoIP_ParamUnitDesc.xml:system/etc/audio_param/VoIP_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/VolumeGainMap_AudioParam.xml:system/etc/audio_param/VolumeGainMap_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/VolumeGainMap_ParamUnitDesc.xml:system/etc/audio_param/VolumeGainMap_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio_param/Volume_AudioParam.xml:system/etc/audio_param/Volume_AudioParam.xml \
    $(LOCAL_PATH)/audio_param/Volume_ParamUnitDesc.xml:system/etc/audio_param/Volume_ParamUnitDesc.xml \
    $(LOCAL_PATH)/audio/audio_policy.conf:system/etc/audio_policy.conf \
    $(LOCAL_PATH)/audio/audio_device.xml:system/etc/audio_device.xml \
    $(LOCAL_PATH)/audio/audio_em.xml:system/etc/audio_em.xml
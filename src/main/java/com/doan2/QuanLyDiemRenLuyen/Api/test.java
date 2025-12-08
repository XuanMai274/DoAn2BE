package com.doan2.QuanLyDiemRenLuyen.Api;

public class test {
    // BACK END

//    @RestController
//    @RequestMapping("/api/conduct-forms")
//    public class ConductFormController {
//
//        @Autowired
//        private ObjectMapper objectMapper;
//
//        @Autowired
//        private CloudinaryService cloudinaryService;
//
//        @Autowired
//        private ConductFormService conductFormService;
//
//        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//        public ResponseEntity<?> createConductForm(
//                @RequestPart("conductForm") String conductFormJson,
//                @RequestPart(value = "detailFiles", required = false) List<MultipartFile> detailFiles,
//                @RequestPart(value = "detailMeta", required = false) List<String> detailMetaList
//        ) {
//            List<String> uploadedPublicIds = new ArrayList<>();
//            try {
//                ConductFormDTO dto = objectMapper.readValue(conductFormJson, ConductFormDTO.class);
//
//                // Validate pair sizes
//                if ((detailFiles != null && detailMetaList == null) || (detailFiles == null && detailMetaList != null)) {
//                    return ResponseEntity.badRequest().body("detailFiles và detailMeta phải có cùng trạng thái (cùng null hoặc cùng non-null).");
//                }
//
//                if (detailFiles != null) {
//                    if (detailFiles.size() != detailMetaList.size()) {
//                        return ResponseEntity.badRequest().body("detailFiles và detailMeta phải có cùng kích thước.");
//                    }
//
//                    for (int i = 0; i < detailFiles.size(); i++) {
//                        MultipartFile file = detailFiles.get(i);
//                        String metaJson = detailMetaList.get(i);
//                        JsonNode metaNode = objectMapper.readTree(metaJson);
//                        String tempId = metaNode.has("tempId") ? metaNode.get("tempId").asText() : null;
//                        Integer criteriaId = metaNode.has("criteriaId") ? metaNode.get("criteriaId").asInt() : null;
//
//                        if (tempId == null) {
//                            return ResponseEntity.badRequest().body("detailMeta[" + i + "] thiếu tempId");
//                        }
//
//                        // Upload file -> get url & public_id
//                        Map<String, Object> uploadRes = cloudinaryService.upload(file, "conduct_form/details");
//                        String url = (String) uploadRes.get("secure_url");
//                        String publicId = (String) uploadRes.get("public_id");
//                        uploadedPublicIds.add(publicId);
//
//                        // Find matching detail in DTO by tempId and set file info
//                        boolean matched = false;
//                        if (dto.getConductFormDetailList() != null) {
//                            for (ConductFormDetailDTO d : dto.getConductFormDetailList()) {
//                                if (tempId.equals(d.getTempId())) {
//                                    d.setFileUrl(url);
//                                    d.setFilePublicId(publicId);
//                                    // also set criteriaId for mapping later if need
//                                    d.setCriteriaId(criteriaId);
//                                    matched = true;
//                                    break;
//                                }
//                            }
//                        }
//                        if (!matched) {
//                            // If no match, cleanup uploaded files and return error
//                            for (String pid : uploadedPublicIds) {
//                                try { cloudinaryService.delete(pid); } catch (Exception ex) { /* log */ }
//                            }
//                            return ResponseEntity.badRequest().body("Không tìm thấy detail tương ứng cho tempId " + tempId);
//                        }
//                    }
//                }
//
//                // Now call service to save DTO -> entity
//                ConductFormDTO saved = conductFormService.addConductForm(dto);
//
//                return ResponseEntity.ok(saved);
//            } catch (Exception ex) {
//                // cleanup uploaded files on error
//                // uploadedPublicIds list filled in try-block; if an exception thrown before list filled, it's empty
//                // NOTE: in real app, log error more precisely
//                // Attempt cleanup
//                // ... you can implement cleanup here if uploadedPublicIds accessible
//                ex.printStackTrace();
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", ex.getMessage()));
//            }
//        }
//    }
}

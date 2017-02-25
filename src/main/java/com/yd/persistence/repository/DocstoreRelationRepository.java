package com.yd.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.DocstoreRelation;

/**
 * @author edys
 * @version 1.0, Apr 23, 2014
 * @since
 */
public interface DocstoreRelationRepository extends
        JpaRepository<DocstoreRelation, String> {

    @Query("SELECT count(r) FROM DocstoreRelation r WHERE r.relationType IN :relationsType")
    public long countByRelationsType(
            @Param("relationsType") List<String> relationsType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.relationType IN :relationsType")
    public List<DocstoreRelation> findAllByRelationsType(
            @Param("relationsType") List<String> relationsType, Pageable paging);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.relationType IN :relationsType ORDER BY r.relationType ASC")
    public List<DocstoreRelation> findAllByRelationsTypeAsc(
            @Param("relationsType") List<String> relationsType, Pageable paging);

    @Query("SELECT count(r) FROM DocstoreRelation r WHERE r.relationType IN :relationsType and r.partyDocstore.uuid IN (:partyDocstoreUuid)")
    public long countByPartyDocstoreAndRelationsType(
            @Param("partyDocstoreUuid") List<String> partyDocstoreUuid,
            @Param("relationsType") List<String> relationsType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.relationType IN :relationsType and r.partyDocstore.uuid IN (:partyDocstoreUuid)")
    public List<DocstoreRelation> findAllByPartyDocstoreAndRelationsType(
            @Param("partyDocstoreUuid") List<String> partyDocstoreUuid,
            @Param("relationsType") List<String> relationsType, Pageable paging);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.relationType IN :relationsType and r.principalDocstore.uuid IN (:principalDocstoreUuid)")
    public List<DocstoreRelation> findAllByPrincipalDocstoreAndRelationsType(
            @Param("principalDocstoreUuid") List<String> principalDocstoreUuid,
            @Param("relationsType") List<String> relationsType, Pageable paging);

    @Query("SELECT count(r) FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN :relationsType")
    public long countByPrincipalDocstoreAndRelationsType(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationsType") List<String> relationsType);

    @Query("SELECT count(r) FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN :relationsType and r.partyDocstore.uuid IN (:partyDocstoreUuid)")
    public long countByPrincipalDocstoreAndRelationsType(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("partyDocstoreUuid") List<String> partyDocstoreUuid,
            @Param("relationsType") List<String> relationsType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.partyDocstore.uuid = :partyDocstoreUuid and r.relationType = :relationType")
    public List<DocstoreRelation> findAllByPartyDocstoreAndRelationType(
            @Param("partyDocstoreUuid") String partyDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN :relationsType")
    public List<DocstoreRelation> findAllByPrincipalDocstoreAndRelationsType(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationsType") List<String> relationsType, Pageable paging);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN :relationsType and r.partyDocstore.uuid IN (:partyDocstoreUuid)")
    public List<DocstoreRelation> findAllByPrincipalDocstoreAndRelationsType(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("partyDocstoreUuid") List<String> partyDocstoreUuid,
            @Param("relationsType") List<String> relationsType, Pageable paging);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN :relationType")
    public List<DocstoreRelation> findAllByPrincipalDocstoreAndRelationTypeIn(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.partyDocstore.uuid = :partyDocstoreUuid and r.relationType IN :relationType")
    public List<DocstoreRelation> findAllByPartyDocstoreAndRelationTypeIn(
            @Param("partyDocstoreUuid") String partyDocstoreUuid,
            @Param("relationType") List<String> relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.partyDocstore.uuid = :partyDocstoreUuid and r.relationType = :relationType")
    public DocstoreRelation findByPartyDocstoreAndRelationType(
            @Param("partyDocstoreUuid") String partyDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType = :relationType")
    public DocstoreRelation findByPrincipalDocstoreAndRelationType(
            @Param("principalDocstoreUuid") String partyDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.partyDocstore.uuid = :partyDocstoreUuid and r.relationType = :relationType")
    public DocstoreRelation findDistinctDocstoreRelation(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("partyDocstoreUuid") String partyDocstoreUuid,
            @Param("relationType") String relationType);

    public DocstoreRelation findByPrincipalDocstoreAndPartyDocstoreAndRelationType(
            Docstore principalDocstore, Docstore partyDocstore,
            String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.partyDocstore.uuid = :partyDocstoreUuid")
    public DocstoreRelation findByPrincipalDocstoreAndPartyDocstore(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("partyDocstoreUuid") String partyDocstoreUuid);

    @Query(value = "SELECT r FROM DocstoreRelation r WHERE (r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN (:relationType)) OR (r.principalDocstore.uuid in (select r2.partyDocstore.uuid from DocstoreRelation r2 where r2.principalDocstore= :principalDocstoreUuid and r2.relationType IN (:relationType))) OR (r.principalDocstore= :principalDocstoreUuid and r.partyDocstore= :principalDocstoreUuid)")
    public List<DocstoreRelation> findByRootPrincipal(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationType") List<String> relationType, Pageable paging);

    @Query(value = "SELECT r FROM DocstoreRelation r WHERE (r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN (:relationType) and r.partyDocstore.uuid IN (:partyDocstoreUuid)) OR (r.principalDocstore.uuid in (select r2.partyDocstore.uuid from DocstoreRelation r2 where r2.principalDocstore= :principalDocstoreUuid and r2.relationType IN (:relationType) and r.partyDocstore.uuid IN (:partyDocstoreUuid))) OR (r.principalDocstore= :principalDocstoreUuid and r.partyDocstore= :principalDocstoreUuid and r.partyDocstore.uuid IN (:partyDocstoreUuid))")
    public List<DocstoreRelation> findByRootPrincipal(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("partyDocstoreUuid") List<String> partyDocstoreUuid,
            @Param("relationType") List<String> relationType, Pageable paging);

    @Query("SELECT count(r) FROM DocstoreRelation r WHERE (r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN (:relationType)) OR (r.principalDocstore.uuid in (select r2.partyDocstore.uuid from DocstoreRelation r2 where r2.principalDocstore= :principalDocstoreUuid and r2.relationType IN (:relationType))) OR (r.principalDocstore= :principalDocstoreUuid and r.partyDocstore= :principalDocstoreUuid)")
    public long countByRootPrincipal(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationType") List<String> relationType);

    @Query("SELECT count(r) FROM DocstoreRelation r WHERE (r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType IN (:relationType) and r.partyDocstore.uuid IN (:partyDocstoreUuid)) OR (r.principalDocstore.uuid in (select r2.partyDocstore.uuid from DocstoreRelation r2 where r2.principalDocstore= :principalDocstoreUuid and r2.relationType IN (:relationType) and r.partyDocstore.uuid IN (:partyDocstoreUuid))) OR (r.principalDocstore= :principalDocstoreUuid and r.partyDocstore= :principalDocstoreUuid and r.partyDocstore.uuid IN (:partyDocstoreUuid))")
    public long countByRootPrincipal(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("partyDocstoreUuid") List<String> partyUuids,
            @Param("relationType") List<String> relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType != :relationType")
    public List<DocstoreRelation> findAllInOtherRelationByPrincipalDocstore(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.partyDocstore.uuid = :partyDocstoreUuid and r.relationType != :relationType")
    public List<DocstoreRelation> findAllInOtherRelationByPartyDocstore(
            @Param("partyDocstoreUuid") String partyDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType = :relationType")
    public List<DocstoreRelation> findAllPrincipalDocstoreAndRelationType(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationType") String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid")
    public List<DocstoreRelation> findAllByPrincipalDocstore(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            Pageable paging);

    public DocstoreRelation findByUuidAndRelationType(String uuid,
            String relationType);

    @Query("SELECT r FROM DocstoreRelation r WHERE r.principalDocstore.uuid = :principalDocstoreUuid and r.relationType = :relationsType")
    public List<DocstoreRelation> findAllByPrincipalDocstoreAndRelationsType(
            @Param("principalDocstoreUuid") String principalDocstoreUuid,
            @Param("relationsType") String relationsType);
}

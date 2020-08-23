package meet_eat.app.repository;

import meet_eat.data.EndpointPath;
import meet_eat.data.entity.relation.Report;

/**
 * Represents the administrative unit that controls access and manipulation of {@link Report reports} within a repository.
 */
public class ReportRepository extends EntityRepository<Report> {

    /**
     * Creates an {@link ReportRepository entity} repository.
     */
    public ReportRepository() {
        super(EndpointPath.REPORTS);
    }
}

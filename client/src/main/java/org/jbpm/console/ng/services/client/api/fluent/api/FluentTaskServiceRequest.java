/*
 * Copyright 2013 JBoss Inc
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
package org.jbpm.console.ng.services.client.api.fluent.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.kie.internal.task.api.UserInfo;
import org.kie.internal.task.api.model.*;

public interface FluentTaskServiceRequest {

    // methods using primitives
    FluentTaskServiceRequest activate(long taskId, String userId);

    FluentTaskServiceRequest claim(long taskId, String userId);

    FluentTaskServiceRequest claim(long taskId, String userId, List<String> groupIds);

    FluentTaskServiceRequest claimNextAvailable(String userId, List<String> groupIds, String language);

    FluentTaskServiceRequest claimNextAvailable(String userId, String language);

    FluentTaskServiceRequest delegate(long taskId, String userId, String targetUserId);

    FluentTaskServiceRequest deleteAttachment(long taskId, long attachmentId);

    FluentTaskServiceRequest deleteContent(long taskId, long contentId);

    FluentTaskServiceRequest deleteFault(long taskId, String userId);

    FluentTaskServiceRequest deleteOutput(long taskId, String userId);

    FluentTaskServiceRequest exit(long taskId, String userId);

    FluentTaskServiceRequest fail(long taskId, String userId, Map<String, Object> faultData);

    FluentTaskServiceRequest forward(long taskId, String userId, String targetEntityId);

    FluentTaskServiceRequest getCompletedTaskByUserId(String userId);

    FluentTaskServiceRequest getPendingSubTasksByParent(long parentId);

    FluentTaskServiceRequest getPendingTaskByUserId(String userId);

    FluentTaskServiceRequest getPriority(long taskId);

    FluentTaskServiceRequest getTaskContent(long taskId);

    FluentTaskServiceRequest getTasksByProcessInstanceId(long processInstanceId);

    FluentTaskServiceRequest release(long taskId, String userId);

    FluentTaskServiceRequest remove(long taskId, String userId);

    FluentTaskServiceRequest removeGroup(String groupId);

    FluentTaskServiceRequest removeUser(String userId);

    FluentTaskServiceRequest resume(long taskId, String userId);

    FluentTaskServiceRequest removeAllTasks();

    FluentTaskServiceRequest removeTaskEventsById(long taskId);

    FluentTaskServiceRequest setPriority(long taskId, int priority);

    FluentTaskServiceRequest setSkipable(long taskId, boolean skipable);

    FluentTaskServiceRequest skip(long taskId, String userId);

    FluentTaskServiceRequest start(long taskId, String userId);

    FluentTaskServiceRequest stop(long taskId, String userId);

    FluentTaskServiceRequest suspend(long taskId, String userId);

    FluentTaskServiceRequest undeployTaskDef(String id);

    FluentTaskServiceRequest isSkipable(long taskId);


    // methods with complex parameters
    FluentTaskServiceRequest addGroup(Group group);

    FluentTaskServiceRequest addUser(User user);

    FluentTaskServiceRequest archiveTasks(List<TaskSummary> tasks);

    FluentTaskServiceRequest complete(long taskId, String userId, Map<String, Object> data);

    FluentTaskServiceRequest deployTaskDef(TaskDef def);

    FluentKieSessionRequest getUserById(String userId);

    List<User> getUsers();

    long addTask(Task task, Map<String, Object> params);

    long addTask(Task task, ContentData data);

    int removeTasks(List<TaskSummary> tasks);

    FluentTaskServiceRequest setFault(long taskId, String userId, FaultData fault);

    FluentTaskServiceRequest setOutput(long taskId, String userId, Object outputContentData);

    FluentTaskServiceRequest setTaskNames(long taskId, List<I18NText> taskNames);

    FluentTaskServiceRequest setUserInfo(UserInfo userInfo);

    FluentTaskServiceRequest addUsersAndGroups(Map<String, User> users, Map<String, Group> groups);

    FluentTaskServiceRequest nominate(long taskId, String userId, List<OrganizationalEntity> potentialOwners);

    long addContent(long taskId, Content content);

    long addContent(long taskId, Map<String, Object> params);

    long addAttachment(long taskId, Attachment attachment, Content content);

    FluentTaskServiceRequest setExpirationDate(long taskId, Date date);

    FluentTaskServiceRequest setDescriptions(long taskId, List<I18NText> descriptions);

    FluentTaskServiceRequest setSubTaskStrategy(long taskId, SubTasksStrategy strategy);

    long addComment(long taskId, Comment comment);



    // methods returning complex parameters
    List<TaskSummary> getActiveTasks(Date since);

    List<TaskSummary> getActiveTasks();

    List<TaskDef> getAllTaskDef(String filter);

    List<TaskSummary> getArchivedTasks();

    List<TaskSummary> getCompletedTasks();

    List<TaskSummary> getCompletedTasks(Date since);

    List<TaskSummary> getCompletedTasksByProcessId(Long processId);

    Group getGroupById(String groupId);

    List<Group> getGroups();

    List<TaskSummary> getSubTasksAssignedAsPotentialOwner(long parentId, String userId, String language);

    List<TaskSummary> getSubTasksByParent(long parentId);

    Task getTaskByWorkItemId(long workItemId);

    TaskDef getTaskDefById(String id);

    Task getTaskById(long taskId);

    List<TaskSummary> getTasksAssignedAsBusinessAdministrator(String userId, String language);

    List<TaskSummary> getTasksAssignedAsExcludedOwner(String userId, String language);

    List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId, List<String> groupIds, String language);

    List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId, List<String> groupIds, String language, int firstResult,
            int maxResults);

    List<TaskSummary> getTasksAssignedAsPotentialOwner(String userId, String language);

    List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatus(String salaboy, List<Status> status, String language);

    List<TaskSummary> getTasksAssignedAsPotentialOwnerByStatusByGroup(String userId, List<String> groupIds, List<Status> status,
            String language);

    List<TaskSummary> getTasksAssignedAsRecipient(String userId, String language);

    List<TaskSummary> getTasksAssignedAsTaskInitiator(String userId, String language);

    List<TaskSummary> getTasksAssignedAsTaskStakeholder(String userId, String language);

    List<TaskSummary> getTasksOwned(String userId);

    List<TaskSummary> getTasksOwned(String userId, List<Status> status, String language);

    List<TaskSummary> getTasksOwnedByExpirationDate(String userId, List<Status> statuses, Date expirationDate);

    List<TaskSummary> getTasksOwnedByExpirationDateOptional(String userId, List<Status> statuses, Date expirationDate);

    List<TaskSummary> getTasksAssignedByGroupsByExpirationDate(List<String> groupIds, String language, Date expirationDate);

    List<TaskSummary> getTasksAssignedByGroupsByExpirationDateOptional(List<String> groupIds, String language, Date expirationDate);

    List<TaskSummary> getTasksByStatusByProcessId(long processInstanceId, List<Status> status, String language);

    List<TaskSummary> getTasksByStatusByProcessIdByTaskName(long processInstanceId, List<Status> status, String taskName,
            String language);
    
    List<TaskEvent> getTaskEventsById(long taskId);

    UserInfo getUserInfo();

    List<Content> getAllContentByTaskId(long taskId);

    Content getContentById(long contentId);

    List<Attachment> getAllAttachmentsByTaskId(long taskId);

    Attachment getAttachmentById(long attachId);

    OrganizationalEntity getOrganizationalEntityById(String entityId);

    Date getExpirationDate(long taskId);

    List<I18NText> getDescriptions(long taskId);

    SubTasksStrategy getSubTaskStrategy(long taskId);

    Task getTaskInstanceById(long taskId);

    List<TaskSummary> getTasksAssignedByGroup(String groupId, String language);

    List<TaskSummary> getTasksAssignedByGroups(List<String> groupIds, String language);

    FluentTaskServiceRequest deleteComment(long taskId, long commentId);

    List<Comment> getAllCommentsByTaskId(long taskId);

    Comment getCommentById(long commentId);
}

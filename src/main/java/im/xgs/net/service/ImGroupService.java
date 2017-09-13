package im.xgs.net.service;

import im.xgs.net.Constants;
import im.xgs.net.exception.ImException;
import im.xgs.net.model.SysGroup;
import im.xgs.net.model.SysGroupFiles;
import im.xgs.net.model.SysGroupMembers;
import im.xgs.net.model.SysGroupMessage;
import im.xgs.net.model.SysGroupNotice;
import im.xgs.net.msg.MsgToType;
import im.xgs.net.util.DateStyle;
import im.xgs.net.util.DateUtil;
import im.xgs.net.util.PinyinUtil;
import im.xgs.net.util.UUIDHexGenerator;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

public class ImGroupService extends BaseService {

	public enum IDENTITY{
		OWNER,MANAGER,NORMAL
	}
	/**
	 * 创建群组
	 * @param groupName
	 * @param decription
	 * @param header
	 * @param owner
	 * @param nickname
	 */
	@Before(Tx.class)
	public SysGroup create(String groupName,String description,String header,String owner,String nickname,Map<String,String> members){
		SysGroup group = new SysGroup();
		group.setId(UUIDHexGenerator.getId());
		group.setGroupName(groupName);
		group.setFullPinyin(PinyinUtil.cn2Spell(groupName));
		group.setBriefPinyin(PinyinUtil.cn2FirstSpell(groupName));
		group.setDecription(description);
		group.setHeader(header);
		group.setOwner(owner);
		group.setVerify(false);
		group.save();
		//加群主-群创建者
		SysGroupMembers member = new SysGroupMembers();
		member.setGroupId(group.getId());
		member.setId(UUIDHexGenerator.getId());
		member.setNickname(nickname);
		member.setUserId(owner);
		member.setUserIdentity(String.valueOf(IDENTITY.OWNER.ordinal()));
		member.save();
		//加其他群成员
		if(members!=null){
			for(String id:members.keySet()){
				if(!id.equals(owner)){
					member.setGroupId(group.getId());
					member.setId(UUIDHexGenerator.getId());
					member.setNickname(members.get(id));
					member.setUserId(id);
					member.setUserIdentity(String.valueOf(IDENTITY.NORMAL.ordinal()));
					member.save();
				}
			}
		}
		return group;
	}
	/**
	 * 拉好友进群
	 * @param groupId
	 * @param members
	 * @throws Exception
	 */
	@Before(Tx.class)
	public void bitchAdd(String groupId, Map<String,String> members) throws Exception{
		if(members!=null){
			for(String id:members.keySet()){
				this.add(id , groupId, members.get(id));
			}
		}
	}
	/**
	 * 加入群组
	 * @param userId
	 * @param groupId
	 * @param nickname
	 */
	public void add(String userId,String groupId,String nickname)throws Exception{
		SysGroupMembers member = this.findByGroupIdAndUserId(groupId, userId);
		if(member!= null){
			throw new ImException(Constants.errorCode.getInt("error.opreation"));
		}
		member = new SysGroupMembers();
		member.setGroupId(groupId);
		member.setId(UUIDHexGenerator.getId());
		member.setNickname(nickname);
		member.setUserId(userId);
		member.setUserIdentity(String.valueOf(IDENTITY.NORMAL.ordinal()));
		member.save();
	}
	/**
	 * 改变用户身份
	 * @param owner
	 * @param userId
	 * @param identity {@link #IDENTITY()}
	 * @param groupId
	 * @throws Exception
	 */
	public void updateIdentity(String owner,String userId,String identity,String groupId) throws Exception{
		if(String.valueOf(IDENTITY.OWNER.ordinal()).equals(identity)){
			throw new ImException(Constants.errorCode.getInt("error.notaddmutiowner"));
		}
		SysGroupMembers member = this.findByGroupIdAndUserId(groupId, owner);
		if(member == null || !member.getUserIdentity().equals(String.valueOf(IDENTITY.OWNER.ordinal()))){
			throw new ImException(Constants.errorCode.getInt("error.opreation"));
		}
		SysGroupMembers user = this.findByGroupIdAndUserId(groupId, userId);
		user.setUserIdentity(identity);
		user.update();
	}
	/**
	 * 退群或解散群
	 * 如果身份为群主，怎解散群，同时删除所有群成员，群文件，群公告，群消息
	 * 否则，仅仅为退群
	 * @param groupId
	 * @param userId
	 * @throws Exception
	 */
	@Before(Tx.class)
	public void quit(String groupId,String userId) throws Exception{
		SysGroupMembers member = this.findByGroupIdAndUserId(groupId, userId);
		if(member == null){
			throw new ImException(Constants.errorCode.getInt("error.opreation"));
		}
		// 如果为群主，解散群
		if(String.valueOf(IDENTITY.OWNER.ordinal()).equals(member.getUserIdentity())){
			SysGroup group = SysGroup.dao.findById(groupId);
			String deleteMembers = " delete from sys_group_members where group_id = ? ";
			String deleteFiles = " delete from sys_group_files where group_id = ? ";
			String deleteNotice = " delete from sys_group_notice where group_id = ? " ;
			String deleteMessage = " delete from sys_group_message where to_group = ? ";
			String deleteOfflineMsg = " delete from sys_offline_message where to_type = ? and from_id = ? ";
			Db.update(deleteMembers, groupId);
			Db.update(deleteFiles, groupId);
			Db.update(deleteNotice, groupId);
			Db.update(deleteMessage, groupId);
			Db.update(deleteOfflineMsg, MsgToType.TOGROUP.ordinal(), groupId);
			group.delete();
		}else{
			// 普通群成员，则退群
			SysGroupMembers user = this.findByGroupIdAndUserId(groupId, userId);
			user.delete();
		}
	}
	/**
	 * 踢出群
	 * @param groupId
	 * @param userId
	 * @param owner
	 * @throws Exception
	 */
	public void kickOutGroup(String groupId, String userId, String owner) throws Exception{
		if(userId.equals(owner)){
			throw new ImException(Constants.errorCode.getInt("error.opreation"));
		}
		SysGroupMembers usermember = this.findByGroupIdAndUserId(groupId, userId);
		SysGroupMembers ownermember = this.findByGroupIdAndUserId(groupId, owner);
		if(usermember == null && usermember == null && String.valueOf(IDENTITY.OWNER.ordinal()).equals(ownermember.getUserIdentity())){
			throw new ImException(Constants.errorCode.getInt("error.opreation"));
		}
		usermember.delete();
	}
	/**
	 * 根据群ID查找群主ID
	 */
	public String getGroupOwnnerId(String groupId){
		String sql = " select user_id groupOwnnerId from sys_group_members where group_id = ? AND user_identity = '0' ";
		Record record = Db.findFirst(sql, groupId);
		return record.getStr("groupOwnnerId");
	}
	/**
	 * 查找用户群组列表
	 * @param userId
	 * @return List<Record> Record字段：groupName,header,description,groupId,ownerId
	 */
	public List<Record> findGroups(String userId){
		String sql = " select sg.group_name groupName,sg.header,sg.decription description,sg.id groupId, sg.owner ownerId from sys_group sg "
				+ " inner join sys_group_members sgm on sg.id = sgm.group_id "
				+ " where sgm.user_id = ? " ;		
	return Db.find(sql,userId);
	}
	/**
	 * 查看群组信息
	 * @param groupId
	 * @return Record字段：groupId,groupName,header,description,owner
	 */
	public Record getGroupInfo(String groupId){
		String sql = " select id groupId,group_name groupName,header,decription description,owner from sys_group where id = ? limit 1 ";
		return Db.findFirst(sql, groupId);
	}
	/**
	 * 查看群组所有成员
	 * @param groupId
	 * @return List<Record> Record字段：groupId,userId,nickname,userIdentify
	 */
	public List<Record> findMember(String groupId){
		String selectSql = " select group_id groupId,user_id userId,nickname,user_identity userIdentity from sys_group_members where group_id = ? order by user_identity asc";
		return Db.find(selectSql, groupId);
	}
	/**
	 * 保存群聊天记录
	 * @param groupId
	 * @param userId
	 * @param msg
	 */
	public void saveMsg(String groupId,String userId,String msg){
		SysGroupMessage sGMsg = new SysGroupMessage();
		sGMsg.setId(UUIDHexGenerator.getId());
		sGMsg.setToGroup(groupId);
		sGMsg.setFromUser(userId);
		sGMsg.setMessage(msg);
		sGMsg.setSendTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		sGMsg.setSearchYm(DateUtil.DateToString(new Date(), DateUtil.DateToString(new Date(), DateStyle.YYYY_MM)));
		sGMsg.save();
	}
	/**
	 * 查看群组聊天记录
	 * @param pageNumber
	 * @param pageSize
	 * @param groupId
	 * @return Page<Record> Record字段：fromUser, message,sendTime
	 */
	public Page<Record> findMsg(int pageNumber,int pageSize,String groupId,String searchYm){
		String select = " select from_user fromUser, message, send_time sendTime ";
		String sqlExceptSelect = " from sys_group_message where to_group = ? and search_ym = ?  order by send_time desc ";
		return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect, groupId,searchYm);
	}
	/**
	 * 发布公告
	 * @param groupId
	 * @param userId
	 * @param notice
	 */
	public void publishNotice(String groupId,String userId,String notice){
		SysGroupNotice groupNotice = new SysGroupNotice();
		groupNotice.setId(UUIDHexGenerator.getId());
		groupNotice.setUserId(userId);
		groupNotice.setGroupId(groupId);
		groupNotice.setContent(notice);
		groupNotice.setPublishTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		groupNotice.save();
	}
	/**
	 * 删除公告
	 * @param id
	 */
	public void deleteNotice(String id){
		String delete = " delete from sys_group_notice where id = ? ";
		Db.update(delete, id);
	}
	/**
	 * 查看公告
	 * @param pageNumber
	 * @param pageSize
	 * @param groupId
	 * @return Page<Record> Record字段：noticeId,userId,content,publishTime
	 */
	public Page<Record> findNotice(int pageNumber,int pageSize,String groupId){
		String select = " select id noticeId,user_id userId,content,publish_time publishTime ";
		String sqlExceptSelect = " from sys_group_notice where group_id = ? order by send_time desc";
		return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect, groupId);
	}
	/**
	 * 上传群文件
	 * @param fileId
	 * @param groupId
	 */
	@Before(Tx.class)
	public void uploadFile(String fileId, String groupId, String userId){
		
		
		SysGroupFiles sGFile = new SysGroupFiles();
		sGFile.setId(UUIDHexGenerator.getId());
		sGFile.setFileId(fileId);
		sGFile.setGroupId(groupId);
		sGFile.setUserId(userId);
		sGFile.setCreateTime(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS));
		sGFile.save();
	}
	/**
	 * 删除群文件
	 * @param fileId
	 */
	@Before(Tx.class)
	public void deleteFile(String fileId){
		String deleteSGFile = " delete from sys_group_files where file_id = ? ";
		Db.update(deleteSGFile, fileId);
	}
	/**
	 * 查看群文件
	 * @param pageNumber
	 * @param pageSize
	 * @param groupId
	 * @return
	 */
	public Page<Record> viewFile(int pageNumber,int pageSize,String groupId){
		
		String select = " select sgf.file_id fileId, sgf.user_id userId";
		String sqlExceptSelect = " from sys_group_files sgf "
								+ " where sgf.group_id = ? order by sgf.create_time desc ";
		
		return Db.paginate(pageNumber, pageSize, select, sqlExceptSelect, groupId);
	}
	/**
	 * 根据groupId 和userId确定唯一的群成员
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public SysGroupMembers findByGroupIdAndUserId(String groupId,String userId){
		String sql = " select * from sys_group_members where group_id = ? and user_id = ? ";
	    return SysGroupMembers.dao.findFirst(sql,groupId,userId);
	}
}

import { Injectable } from "@angular/core";
import { Classroom } from "../models/Classroom";
import { Routes } from "../config/routes.config";
import { Observable } from "rxjs/Observable";
import { HttpClient } from "@angular/common/http";

/**
 * Az tantermekhez tartozó service osztály
 */
@Injectable()
export class ClassroomService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(Routes.getUrl(Routes.CLASSROOM_GET_ALL))
    );
  }

  findByName(roomName: string): Observable<Classroom> {
    return <Observable<Classroom>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_NAME) + "/" + roomName
      )
    );
  }

  findByBuildingName(buildingName: string): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_BUILDING_NAME) +
          "/" +
          buildingName
      )
    );
  }

  findByNameAndBuildingName(
    name: string,
    buildingName: string
  ): Observable<Classroom> {
    return <Observable<Classroom>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_NAME_AND_BUILDING_NAME) +
          `?name=${name}&buildingName=${buildingName}`
      )
    );
  }

  findByHasPC(hasPC: boolean): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_HAS_PC) + "/" + hasPC
      )
    );
  }

  findByHasProjector(hasProjector: boolean): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_HAS_PROJECTOR) +
          "/" +
          hasProjector
      )
    );
  }

  findByChairsLessThan(chairs: number): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_CHAIRS_LESS_THAN) + "/" + chairs
      )
    );
  }

  findByChairsGreaterThan(chairs: number): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_CHAIRS_GREATER_THAN) +
          "/" +
          chairs
      )
    );
  }

  findByChairsBetween(from: number, to: number): Observable<Classroom[]> {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_FIND_BY_CHAIRS_BETWEEN) +
          `?from=${from}&to=${to}`
      )
    );
  }

  createClassroom(classroom: Classroom) {
    return <Observable<Classroom[]>>(
      this.http.post(
        Routes.getUrl(Routes.CLASSROOM_CREATE_CLASSROOM),
        classroom
      )
    );
  }

  deleteByNameAndBuildingName(name: string, buildingName: string) {
    return <Observable<Classroom[]>>(
      this.http.get(
        Routes.getUrl(Routes.CLASSROOM_DELETE_BY_NAME_AND_BUILDING_NAME) +
          `?name=${name}&buildingName=${buildingName}`
      )
    );
  }
}

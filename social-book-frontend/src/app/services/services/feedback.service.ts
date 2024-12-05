/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { getFeedbacksByBookId } from '../fn/feedback/get-feedbacks-by-book-id';
import { GetFeedbacksByBookId$Params } from '../fn/feedback/get-feedbacks-by-book-id';
import { PageResponseFeedbackResponse } from '../models/page-response-feedback-response';
import { saveFeedBack } from '../fn/feedback/save-feed-back';
import { SaveFeedBack$Params } from '../fn/feedback/save-feed-back';


/**
 * Feedback API
 */
@Injectable({ providedIn: 'root' })
export class FeedbackService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `saveFeedBack()` */
  static readonly SaveFeedBackPath = '/feedback';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `saveFeedBack()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveFeedBack$Response(params: SaveFeedBack$Params, context?: HttpContext): Observable<StrictHttpResponse<number>> {
    return saveFeedBack(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `saveFeedBack$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  saveFeedBack(params: SaveFeedBack$Params, context?: HttpContext): Observable<number> {
    return this.saveFeedBack$Response(params, context).pipe(
      map((r: StrictHttpResponse<number>): number => r.body)
    );
  }

  /** Path part for operation `getFeedbacksByBookId()` */
  static readonly GetFeedbacksByBookIdPath = '/feedback/book/{book-id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getFeedbacksByBookId()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFeedbacksByBookId$Response(params: GetFeedbacksByBookId$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFeedbackResponse>> {
    return getFeedbacksByBookId(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getFeedbacksByBookId$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFeedbacksByBookId(params: GetFeedbacksByBookId$Params, context?: HttpContext): Observable<PageResponseFeedbackResponse> {
    return this.getFeedbacksByBookId$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseFeedbackResponse>): PageResponseFeedbackResponse => r.body)
    );
  }

}
